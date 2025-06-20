package com.somnionocte.atomic_components.extensions

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.spring
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
import com.somnionocte.compose_extensions.animatableColorAs
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
    specIn: FiniteAnimationSpec<Color> = tween(100, 0, EaseOutExpo),
    specOut: FiniteAnimationSpec<Color> = spring(1f, 200f),
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

enum class InteractionPressState { Default, Pressed, Hovered, Focused }

@Composable
fun InteractionSource.animatableColorState(
    color: (InteractionPressState) -> Color,
    specIn: FiniteAnimationSpec<Color> = tween(100, 0, EaseOutExpo),
    specOut: FiniteAnimationSpec<Color> = spring(1f, 200f),
): State<Color> {
    val isPressed by collectIsPressedAsState()
    val isHovered by collectIsHoveredAsState()
    val isFocused by collectIsFocusedAsState()

    return animatableColorAs({ if(isPressed) specIn else specOut }) {
        when {
            isPressed -> color(InteractionPressState.Pressed)
            isHovered -> color(InteractionPressState.Hovered)
            isFocused -> color(InteractionPressState.Focused)
            else -> color(InteractionPressState.Default)
        }
    }.asState()
}