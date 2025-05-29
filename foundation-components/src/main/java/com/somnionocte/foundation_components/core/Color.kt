package com.somnionocte.foundation_components.core

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

fun Color.mix(
    @FloatRange(from = 0.0, to = 1.0) ratio: Float,
    color: Color
) = Color(ColorUtils.blendARGB(this.toArgb(), color.toArgb(), ratio))