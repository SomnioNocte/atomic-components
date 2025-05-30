package com.somnionocte.foundation_components.extensions

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color

fun Color.mix(
    @FloatRange(from = 0.0, to = 1.0) ratio: Float,
    color: Color
): Color {
    val inverseRatio = 1 - ratio

    val r = red * inverseRatio + color.red * ratio
    val g = green * inverseRatio + color.green * ratio
    val b = blue * inverseRatio + color.blue * ratio
    val a = alpha * inverseRatio + color.alpha * ratio

    return Color(r, g, b, a.coerceIn(0f..1f))
}