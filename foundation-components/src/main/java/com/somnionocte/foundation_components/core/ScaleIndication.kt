package com.somnionocte.foundation_components.core

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.util.lerp
import kotlinx.coroutines.Job
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

object ScaleIndication : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return ScaleIndicationNode(interactionSource)
    }

    override fun hashCode(): Int = -1

    override fun equals(other: Any?) = other === this
}

private class ScaleIndicationNode(
    private val interactionSource: InteractionSource
) : Modifier.Node(), DrawModifierNode {
    val animatedScalePercent = Animatable(0f)

    private suspend fun animateToPressed() {
        animatedScalePercent.animateTo(1f, spring(1f, 3500f))
    }

    private suspend fun animateToResting() {
        while (animatedScalePercent.isRunning) awaitFrame()
        animatedScalePercent.animateTo(0f, spring(1f, 500f))
    }

    private val isPressed = MutableStateFlow(false)

    var job: Job? = null
    override fun onDetach() { job?.cancel() }
    override fun onAttach() {
        job = coroutineScope.launch {
            launch {
                isPressed.asStateFlow()
                    .filter { it }
                    .collectLatest {
                        animateToPressed()
                        while (isPressed.value) awaitFrame()
                        animateToResting()
                    }
            }

            launch {
                interactionSource.interactions.collect { interaction ->
                    when (interaction) {
                        is PressInteraction.Press -> isPressed.value = true
                        else -> isPressed.value = false
                    }
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        val ratio = (68f / size.maxDimension).coerceAtMost(.1f)

        scale(lerp(1f, 1f - ratio, animatedScalePercent.value)) {
            this@draw.drawContent()
        }
    }
}