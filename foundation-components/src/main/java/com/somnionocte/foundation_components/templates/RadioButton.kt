package com.somnionocte.foundation_components.templates

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
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

@Composable
fun TemplateRadioButton(
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Color.Unspecified,
    backgroundCheckedColor: Color = Color.Unspecified,
    checkColor: Color = Color.Unspecified,
    borderStroke: BorderStroke? = null,
    borderStrokeChecked: BorderStroke? = borderStroke,
    shape: Shape = RoundedCornerShape(50),
    thumbShape: Shape = RoundedCornerShape(50),
    minWidth: Dp = 28.dp,
    minHeight: Dp = minWidth,
    checkSize: Dp = 16.dp,
    interactionSource: MutableInteractionSource? = null,
    margin: PaddingValues = PaddingValues(4.dp, 6.dp),
    animationSpec: FiniteAnimationSpec<Float> = spring(1f, 700f),
    indication: Indication? = LocalIndication.current
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val transition by animateFloatAsState(if(checked) 1f else 0f, animationSpec)

    val disabledTransition by animateFloatAsState(if(enabled) 1f else .5f, animationSpec)

    FoundationToggleable(
        state = checked,
        onClick = { onCheckedChange() },
        modifier = modifier.padding(margin),
        enabled = enabled,
        minWidth = minWidth,
        minHeight = minHeight,
        interactionSource = interactionSource,
        indication = indication,
        role = Role.Switch,
        designModifier = Modifier
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
            }
            .clip(shape)
            .graphicsLayer {
                scaleX = transition
                scaleY = scaleX
            }
    ) {
        Box(Modifier
            .align(Alignment.Center)
            .wrapContentSize(Alignment.Center)
            .size(checkSize)
            .drawWithCache {
                val thumbOutline = thumbShape.createOutline(size, layoutDirection, Density(density))

                onDrawBehind {
                    drawOutline(thumbOutline, lerp(Color.Transparent, checkColor, transition))
                }
            })
    }
}