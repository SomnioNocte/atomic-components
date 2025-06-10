package com.somnionocte.atomic_components

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.somnionocte.atomic_components.extensions.animatableStateOf

@Composable
fun AtomicLinearProgress(
    progress: () -> Float,
    color: Color,
    trackColor: Color,
    modifier: Modifier = Modifier.padding(vertical = 8.dp),
    shapeRadius: Dp = 0.dp,
    trackPadding: PaddingValues = PaddingValues(0.dp),
    width: Dp = 240.dp,
    height: Dp = 5.dp,
    trackShapeRadius: Dp = shapeRadius,
    spec: FiniteAnimationSpec<Float> = spring(1f, 1000f),
    draw: (DrawScope.() -> Unit)? = null
) {
    val animatedProgress by animatableStateOf({ spec }) { progress().coerceIn(0f..1f) }

    Canvas(modifier.size(width, height).clip(RoundedCornerShape(shapeRadius))) {
        drawRect(color)

        val offset = Offset(
            trackPadding.calculateStartPadding(layoutDirection).toPx(),
            trackPadding.calculateTopPadding().toPx()
        )

        drawRoundRect(
            trackColor,
            offset,
            size.run { Size(
                lerp(0f, this.width - offset.x - trackPadding.calculateEndPadding(layoutDirection).toPx(), animatedProgress),
                this.height - offset.y - trackPadding.calculateBottomPadding().toPx()
            ) },
            cornerRadius = CornerRadius(trackShapeRadius.toPx(), trackShapeRadius.toPx())
        )

        draw?.invoke(this)
    }
}

@Composable
fun AtomicLinearProgress(
    progress: () -> Float,
    color: Color,
    trackColor: Color,
    trackGap: Dp,
    modifier: Modifier = Modifier.padding(vertical = 8.dp),
    shapeRadius: Dp = 0.dp,
    width: Dp = 240.dp,
    height: Dp = 5.dp,
    trackShapeRadius: Dp = shapeRadius,
    spec: FiniteAnimationSpec<Float> = spring(1f, 1000f),
    draw: (DrawScope.() -> Unit)? = null
) {
    val animatedProgress by animatableStateOf({ spec }) { progress().coerceIn(0f..1f) }

    Canvas(modifier.size(width, height)) {
        val gapOffset = Offset(trackGap.roundToPx() * (1f - animatedProgress), 0f)

        val trackWidth = lerp(0f, size.width, animatedProgress) - trackGap.roundToPx() * animatedProgress

        drawRoundRect(
            trackColor,
            size = size.copy(trackWidth),
            cornerRadius = CornerRadius(trackShapeRadius.toPx(), trackShapeRadius.toPx())
        )

        drawRoundRect(
            color,
            gapOffset.copy(gapOffset.x + trackWidth),
            size = size.copy(lerp(size.width - gapOffset.x, 0f, animatedProgress)),
            cornerRadius = CornerRadius(shapeRadius.toPx(), shapeRadius.toPx())
        )

        draw?.invoke(this)
    }
}