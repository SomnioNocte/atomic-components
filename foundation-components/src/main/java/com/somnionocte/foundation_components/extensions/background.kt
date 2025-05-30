package com.somnionocte.foundation_components.extensions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.unit.Density

fun Modifier.background(color: () -> Color) = drawBehind { drawRect(color()) }
fun Modifier.background(color: Animatable<Color, AnimationVector4D>) = drawBehind { drawRect(color.value) }

fun Modifier.background(shape: () -> Shape, color: () -> Color) = drawWithCache {
    val outline = shape().createOutline(size, layoutDirection, Density(density))
    onDrawBehind { drawOutline(outline, color()) }
}
fun Modifier.background(shape: () -> Shape, color: Animatable<Color, AnimationVector4D>) =
    background(shape) { color.value }