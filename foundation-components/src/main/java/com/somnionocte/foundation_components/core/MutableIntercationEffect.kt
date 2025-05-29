package com.somnionocte.foundation_components.core

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.android.awaitFrame
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@Composable
fun MutableInteractionSource.onPress(
    onPress: suspend (awaitRelease: suspend () -> Unit) -> Unit,
) {
    val isPressed by collectIsPressedAsState()

    LaunchedEffect(this) {
        launch {
            snapshotFlow { isPressed }
                .filter { it }
                .collectLatest { onPress { while (isPressed) awaitFrame() } }
        }
    }
}

@Composable
fun InteractionSource.animateColorAsState(
    default: Color,
    pressed: Color,
    hovered: Color = default,
    focused: Color = default,
    specIn: FiniteAnimationSpec<Color> = tween(150, 0, EaseOutCirc),
    specOut: FiniteAnimationSpec<Color> = tween(550, 0, Ease),
): State<Color> {
    val isPressed by collectIsPressedAsState()
    val isHovered by collectIsHoveredAsState()
    val isFocused by collectIsFocusedAsState()

    return animateColorAsState(
        if(isPressed) pressed
        else
            if(isHovered) hovered
            else
                if(isFocused) focused
                else default,
        animationSpec = if(isPressed) specIn else specOut
    )
}