package com.somnionocte.foundation_components.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer

fun Modifier.clip(shape: () -> Shape) = graphicsLayer {
    this.clip = true
    this.shape = shape()
}