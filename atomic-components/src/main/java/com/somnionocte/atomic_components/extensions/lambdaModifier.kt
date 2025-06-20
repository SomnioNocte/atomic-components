package com.somnionocte.atomic_components.extensions

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.drawBackgroundColor(shape: () -> Shape = { RectangleShape }, color: () -> Color) = drawBehind {
    val shape = shape().takeIf { it != RectangleShape }
    if(shape != null) {
        val outline = shape.createOutline(size, layoutDirection, Density(density))
        drawOutline(outline, color())
    } else {
        drawRect(color())
    }
}

fun Modifier.drawBackgroundBrush(shape: () -> Shape = { RectangleShape }, brush: () -> Brush) = drawBehind {
    val shape = shape().takeIf { it != RectangleShape }
    if(shape != null) {
        val outline = shape.createOutline(size, layoutDirection, Density(density))
        drawOutline(outline, brush())
    } else {
        drawRect(brush())
    }
}

fun Modifier.drawBorderColor(
    shape: () -> Shape = { RectangleShape },
    width: () -> Dp = { 1.dp },
    color: () -> Color
) = drawWithCache {
    val outline = shape().createOutline(size, layoutDirection, Density(density))
    onDrawBehind { drawOutline(outline, color(), style = Stroke(width().toPx())) }
}

fun Modifier.drawBorderBrush(
    shape: () -> Shape = { RectangleShape },
    width: () -> Dp = { 1.dp },
    brush: () -> Brush
) = drawWithCache {
    val outline = shape().createOutline(size, layoutDirection, Density(density))
    onDrawBehind { drawOutline(outline, brush(), style = Stroke(width().toPx())) }
}

fun Modifier.drawBorder(
    shape: () -> Shape = { RectangleShape },
    borderStroke: () -> BorderStroke?,
) = drawWithCache {
    borderStroke()?.let { border ->
        val outline = shape().createOutline(size, layoutDirection, Density(density))
        onDrawBehind { drawOutline(outline, border.brush, style = Stroke(border.width.toPx())) }
    } ?: onDrawBehind {

    }
}

fun Modifier.clip(shape: () -> Shape) = graphicsLayer {
    this.clip = true
    this.shape = shape()
}