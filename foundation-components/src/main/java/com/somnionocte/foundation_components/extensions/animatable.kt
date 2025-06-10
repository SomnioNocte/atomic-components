package com.somnionocte.foundation_components.extensions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Animated state without calling recomposition. Only work with compose states.
 * */
@Composable
fun animatableStateOf(
    animatable: Animatable<Float, AnimationVector1D>,
    spec: @DisallowComposableCalls () -> AnimationSpec<Float> = { spring() },
    onAnimationFinished: suspend () -> Unit = {  },
    value: @DisallowComposableCalls () -> Float
): State<Float> {
    val state by remember { derivedStateOf { value() } }
    val spec by remember { derivedStateOf { spec() } }

    LaunchedEffect(Unit) {
        snapshotFlow { state }.collectLatest { value ->
            launch {
                animatable.animateTo(value, spec)
                onAnimationFinished()
            }
        }
    }

    return animatable.asState()
}

/**
 * Animated state without calling recomposition. Only work with compose states.
 * */
@Composable
fun animatableStateOf(
    spec: () -> AnimationSpec<Float> = { spring(1f, 3000f) },
    initialValue: Float = 0f,
    onAnimationFinished: suspend () -> Unit = {  },
    value: () -> Float
): State<Float> {
    val animatable = remember { Animatable(initialValue) }
    return animatableStateOf(animatable, spec, onAnimationFinished, value)
}

/**
 * Animated state without calling recomposition. Only work with compose states.
 * */
@Composable
fun animatableColorStateOf(
    animatable: Animatable<Color, AnimationVector4D>,
    spec: @DisallowComposableCalls () -> AnimationSpec<Color> = { spring() },
    onAnimationFinished: () -> Unit = {  },
    value: @DisallowComposableCalls () -> Color
): State<Color> {
    val state by remember { derivedStateOf { value() } }
    val spec by remember { derivedStateOf { spec() } }

    LaunchedEffect(Unit) {
        snapshotFlow { state }.collectLatest { value ->
            launch {
                animatable.animateTo(value, spec)
                onAnimationFinished()
            }
        }
    }

    return animatable.asState()
}

/**
 * Animated state without calling recomposition. Only work with compose states.
 * */
@Composable
fun animatableColorStateOf(
    spec: () -> AnimationSpec<Color> = { spring() },
    initialValue: Color = Color.Transparent,
    onAnimationFinished: () -> Unit = {  },
    value: () -> Color
): State<Color> {
    val animatable = remember { androidx.compose.animation.Animatable(initialValue) }
    return animatableColorStateOf(animatable, spec, onAnimationFinished, value)
}