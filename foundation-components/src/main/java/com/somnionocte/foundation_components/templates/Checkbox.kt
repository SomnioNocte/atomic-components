package com.somnionocte.foundation_components.templates

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.somnionocte.foundation_components.FoundationToggleable
import com.somnionocte.foundation_components.Icon
import com.somnionocte.foundation_components.extensions.CheckDrawingCache
import com.somnionocte.foundation_components.extensions.drawCheck

@Composable
fun TemplateCheckbox(
    state: ToggleableState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Color.Unspecified,
    backgroundCheckedColor: Color = Color.Unspecified,
    borderStroke: BorderStroke? = null,
    borderStrokeChecked: BorderStroke? = borderStroke,
    checkColor: Color = Color.Unspecified,
    shape: Shape = RoundedCornerShape(40),
    minWidth: Dp = 32.dp,
    minHeight: Dp = minWidth,
    interactionSource: MutableInteractionSource? = null,
    margin: PaddingValues = PaddingValues(6.dp),
    animationSpec: FiniteAnimationSpec<Float> = spring(1f, 700f),
    strokeWidthPx: Float = 8f,
    checkboxSize: Dp = 20.dp,
    indication: Indication? = LocalIndication.current,
    icon: @Composable BoxScope.(transition: () -> Float) -> Unit = { transition ->
        val checkCache = remember { CheckDrawingCache() }
        val indeterminateTransition by animateFloatAsState(
            if(state == ToggleableState.Indeterminate) 1f else 0f,
            animationSpec
        )

        Canvas(Modifier.align(Alignment.Center).wrapContentSize(Alignment.Center).requiredSize(checkboxSize)) {
            drawCheck(
                lerp(Color.Transparent, checkColor, transition()),
                transition(),
                indeterminateTransition,
                strokeWidthPx,
                checkCache
            )
        }
    }
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val transition by animateFloatAsState(when(state) {
        ToggleableState.Off -> 0f
        else -> 1f
    }, animationSpec)

    val disabledTransition by animateFloatAsState(if(enabled) 1f else .5f, animationSpec)

    FoundationToggleable(
        state = state,
        onClick = onClick,
        modifier = modifier.padding(margin),
        enabled = enabled,
        minWidth = minWidth,
        minHeight = minHeight,
        interactionSource = interactionSource,
        indication = indication,
        designModifier = Modifier
            //.clip(shape)
            .graphicsLayer { alpha = disabledTransition }
            .drawWithCache {
                val shapeOutline = shape.createOutline(size, layoutDirection, Density(density))

                onDrawBehind {
                    drawOutline(shapeOutline, lerp(backgroundColor, backgroundCheckedColor, transition))

                    if(borderStroke == borderStrokeChecked) {
                        borderStroke?.let { drawOutline(shapeOutline, it.brush, style = Stroke(it.width.toPx())) }
                    } else {
                        borderStroke?.let {
                            drawOutline(shapeOutline,it.brush, style = Stroke(it.width.toPx()), alpha = 1f - transition)
                        }
                        borderStrokeChecked?.let {
                            drawOutline(shapeOutline,it.brush, style = Stroke(it.width.toPx()), alpha = transition)
                        }
                    }
                }
            },
        role = Role.Checkbox
    ) {
        icon { transition }
    }
}

@Composable
fun TemplateCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Color.Unspecified,
    backgroundCheckedColor: Color = Color.Unspecified,
    checkColor: Color = Color.Unspecified,
    borderStroke: BorderStroke? = null,
    borderStrokeChecked: BorderStroke? = borderStroke,
    shape: Shape = RoundedCornerShape(40),
    minWidth: Dp = 32.dp,
    minHeight: Dp = minWidth,
    interactionSource: MutableInteractionSource? = null,
    margin: PaddingValues = PaddingValues(6.dp),
    animationSpec: FiniteAnimationSpec<Float> = spring(1f, 700f),
    indication: Indication? = LocalIndication.current,
    checkboxSize: Dp = 20.dp,
    strokeWidthPx: Float = 8f,
    icon: @Composable BoxScope.(transition: () -> Float) -> Unit = { transition ->
        val checkCache = remember { CheckDrawingCache() }

        Canvas(Modifier.align(Alignment.Center).wrapContentSize(Alignment.Center).requiredSize(checkboxSize)) {
            drawCheck(
                lerp(Color.Transparent, checkColor, transition()),
                transition(),
                0f,
                strokeWidthPx,
                checkCache
            )
        }
    }
) {
    TemplateCheckbox(
        state = if(checked) ToggleableState.On else ToggleableState.Off,
        onClick = { onCheckedChange(!checked) },
        modifier = modifier,
        enabled = enabled,
        backgroundColor = backgroundColor,
        backgroundCheckedColor = backgroundCheckedColor,
        borderStroke = borderStroke,
        borderStrokeChecked = borderStrokeChecked,
        shape = shape,
        minWidth = minWidth,
        minHeight = minHeight,
        interactionSource = interactionSource,
        margin = margin,
        animationSpec = animationSpec,
        indication = indication,
        icon = icon
    )
}