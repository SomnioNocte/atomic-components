package com.somnionocte.atomic_components.extensions

import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Density

internal fun Modifier.surface(
    color: Brush? = null,
    shape: Shape = RectangleShape,
    border: BorderStroke? = null
) = drawWithCache {
    val density = Density(density)

    val outline =
        if(shape != RectangleShape || border != null) shape.createOutline(size, layoutDirection, density)
        else null

    onDrawBehind {
        if(shape == RectangleShape) {
            if(color != null)
                drawRect(color)
        } else {
            drawOutline(outline!!, color ?: SolidColor(Color.Transparent))
        }

        if(border != null)
            drawOutline(outline!!, border.brush, style = Stroke(border.width.toPx()))
    }
}