package com.somnionocte.atomic_components.templates

import androidx.compose.animation.core.EaseOutExpo
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.somnionocte.atomic_components.components.AtomicButton
import com.somnionocte.atomic_components.core.LocalContentColor
import com.somnionocte.atomic_components.extensions.animateColorAsState
import com.somnionocte.atomic_components.extensions.drawBackgroundColor
import com.somnionocte.atomic_components.extensions.drawBorder

@Composable
fun TemplateButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    contentColor: Color = LocalContentColor.current,
    defaultColor: Color,
    pressedColor: Color = lerp(defaultColor, contentColor, .3f),
    hoveredColor: Color = lerp(defaultColor, pressedColor, .35f),
    focusedColor: Color = lerp(defaultColor, pressedColor, .25f),
    specIn: FiniteAnimationSpec<Color> = tween(100, 0, EaseOutExpo),
    specOut: FiniteAnimationSpec<Color> = spring(1f, 200f),
    shape: Shape = RectangleShape,
    borderStroke: BorderStroke? = null,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = LocalIndication.current,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    minSize: DpSize = DpSize(24.dp, 24.dp),
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val opacity by animateFloatAsState(if(enabled) 1f else .65f, spring(1f, 500f))

    val color by interactionSource.animateColorAsState(defaultColor, pressedColor, hoveredColor, focusedColor, specIn, specOut)

    AtomicButton(
        onClick = onClick,
        content = content,
        modifier = modifier,
        designModifier = Modifier
            .graphicsLayer { alpha = opacity }
            .drawBorder({ shape }, { borderStroke })
            .drawBackgroundColor(shape = { shape }, color = { color })
            .padding(contentPadding),
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
        contentColor = contentColor,
        minSize = minSize
    )
}