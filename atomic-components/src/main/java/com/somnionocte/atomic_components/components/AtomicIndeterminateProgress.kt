package com.somnionocte.atomic_components.components

import androidx.compose.animation.core.Ease
import androidx.compose.animation.core.EaseInCubic
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

@Composable
fun AtomicLinearIndeterminateProgress(
    color: Color,
    trackColor: Color,
    modifier: Modifier = Modifier.padding(vertical = 8.dp),
    shapeRadius: Dp = 0.dp,
    trackPadding: PaddingValues = PaddingValues(0.dp),
    width: Dp = 240.dp,
    height: Dp = 5.dp,
    trackShapeRadius: Dp = shapeRadius,
    spec1: InfiniteRepeatableSpec<Float> = infiniteRepeatable(tween(2000, 0, Ease)),
    spec2: InfiniteRepeatableSpec<Float> = infiniteRepeatable(tween(1000, 1000, Ease))
) {
    val animationState = rememberInfiniteTransition()
    val animated1Progress by animationState.animateFloat(0f, 1f, spec1)
    val animated2Progress by animationState.animateFloat(0f, 1f, spec2)

    Canvas(modifier.size(width, height).clip(RoundedCornerShape(shapeRadius))) {
        drawRect(color)

        val initialX = trackPadding.calculateStartPadding(layoutDirection).toPx()
        val initialWidth = size.width - initialX - trackPadding.calculateEndPadding(layoutDirection).toPx()

        val offset1 = Offset(
            lerp(
                initialX,
                initialWidth,
                EaseInOut.transform(animated1Progress)
            ),
            trackPadding.calculateTopPadding().toPx()
        )

        val offset2 = Offset(
            lerp(
                initialX,
                initialWidth,
                EaseInCubic.transform(animated2Progress)
            ),
            trackPadding.calculateTopPadding().toPx()
        )

        drawRoundRect(
            trackColor,
            offset1,
            size.run { Size(
                lerp(0f, initialWidth * .35f, EaseOutBack.transform(animated1Progress)).let {
                    it.coerceAtLeast(offset1.x - it)
                },
                this.height - offset1.y - trackPadding.calculateBottomPadding().toPx()
            ) },
            cornerRadius = CornerRadius(trackShapeRadius.toPx(), trackShapeRadius.toPx())
        )

        drawRoundRect(
            trackColor,
            offset2.copy(offset2.x),
            size.run { Size(
                lerp(0f, initialWidth * .5f, Ease.transform(animated2Progress)).let {
                    it.coerceAtLeast(offset2.x - it)
                },
                this.height - offset2.y - trackPadding.calculateBottomPadding().toPx()
            ) },
            cornerRadius = CornerRadius(trackShapeRadius.toPx(), trackShapeRadius.toPx())
        )
    }
}