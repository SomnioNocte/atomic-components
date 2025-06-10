package com.somnionocte.atomic_components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.somnionocte.atomic_components.extensions.animatableStateOf
import com.somnionocte.atomic_components.extensions.mix
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.pow

fun DrawScope.drawCoreScrollBar(
    progress: Float,
    fillSpace: Float,
    color: Color,
    opacity: Float,
    contentPadding: PaddingValues,
    overscroll: Float
) {
    if(fillSpace != 1f && opacity != 0f) {
        val padding = (contentPadding.calculateTopPadding() + contentPadding.calculateBottomPadding() + 24.dp).toPx()

        val maxScrollBarHeight = size.height - padding

        drawRoundRect(
            Color.Transparent.mix(opacity, color),
            Offset(
                x = size.width - contentPadding.calculateEndPadding(LayoutDirection.Ltr).toPx(),
                y = contentPadding.calculateTopPadding().toPx() + 12.dp.toPx() + progress * (maxScrollBarHeight - (fillSpace * overscroll) * maxScrollBarHeight)
            ),
            Size(
                3.5.dp.toPx(),
                (fillSpace * overscroll) * maxScrollBarHeight
            ),
            cornerRadius = CornerRadius(16f, 16f)
        )
    }
}

fun Modifier.coreScrollbar(
    scrollState: ScrollState,
    contentPadding: PaddingValues = PaddingValues(10.dp),
    color: Color = Color.Gray.copy(.5f),
    fadeSpecIn: AnimationSpec<Float> = spring(1f, 1000f),
    fadeSpecOut: AnimationSpec<Float> = spring(1f, 200f),
    delayToFadeOut: Long = 750L,
    overscrollOffset: (() -> Float)? = null
) = composed {
    val density = LocalDensity.current

    val opacity = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        snapshotFlow { scrollState.isScrollInProgress }
            .debounce { if(it) 0L else delayToFadeOut }
            .collect {
                if(it) opacity.animateTo(1f, fadeSpecIn)
                else launch { opacity.animateTo(0f, fadeSpecOut) }
            }
    }

    val progress by animatableStateOf(
        spec = { spring(1f, 8000f) },
        value = { scrollState.value.toFloat() / scrollState.maxValue }
    )

    val heightFraction by animatableStateOf(
        spec = { spring(1f, 8000f) },
        value = {
            val fraction = (scrollState.viewportSize.toFloat() / (scrollState.maxValue * density.density + scrollState.viewportSize))
                .coerceIn(.1f, 1f)
            fraction.pow(.65f)
        }
    )

    val overscroll by remember { derivedStateOf {
        overscrollOffset
            ?.run { 1f - (abs(invoke()) / scrollState.viewportSize).coerceIn(0f, .65f) }
            ?: 1f
    } }

    drawWithContent {
        drawContent()
        drawCoreScrollBar(progress, heightFraction, color, opacity.value, contentPadding, overscroll)
    }
}

fun Modifier.coreScrollbar(
    scrollState: ScrollState,
    fadeSpecIn: AnimationSpec<Float> = spring(1f, 3500f),
    fadeSpecOut: AnimationSpec<Float> = spring(1f, 300f),
    delayToFadeOut: Long = 500L,
    drawScrollBar: DrawScope.(progress: Float, fillSpace: Float, opacity: Float) -> Unit
) = composed {
    val density = LocalDensity.current

    val opacity = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        snapshotFlow { scrollState.isScrollInProgress }
            .debounce { if(it) 0L else delayToFadeOut }
            .collectLatest { launch {
                if(it) opacity.animateTo(1f, fadeSpecIn)
                else opacity.animateTo(0f, fadeSpecOut)
            } }
    }

    val progress by animatableStateOf(
        spec = { spring(1f, 8000f) },
        value = { scrollState.value.toFloat() / scrollState.maxValue }
    )

    val heightFraction by animatableStateOf(
        spec = { spring(1f, 8000f) },
        value = {
            val fraction = (scrollState.viewportSize.toFloat() / (scrollState.maxValue * density.density + scrollState.viewportSize))
                .coerceIn(.1f, 1f)
            fraction.pow(.65f)
        }
    )

    drawWithContent {
        drawContent()
        drawScrollBar(this, progress, heightFraction, opacity.value)
    }
}
