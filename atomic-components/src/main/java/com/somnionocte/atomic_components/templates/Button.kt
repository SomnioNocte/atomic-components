package com.somnionocte.atomic_components.templates

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.somnionocte.atomic_components.AtomicButton
import com.somnionocte.atomic_components.LocalTextStyle
import com.somnionocte.atomic_components.core.LocalContentColor
import com.somnionocte.atomic_components.extensions.animateColorAsState

@Composable
fun TemplateButton(
    onClick: () -> Unit,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    pressedBackgroundColor: Color = backgroundColor,
    disabledBackgroundColor: Color = backgroundColor,
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    contentColor: Color = LocalContentColor.current,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    textStyle: TextStyle = LocalTextStyle.current,
    minWidth: Dp = 48.dp,
    minHeight: Dp = 48.dp,
    indication: Indication? = LocalIndication.current,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    val color by interactionSource.animateColorAsState(
        if(enabled) backgroundColor else disabledBackgroundColor,
        pressedBackgroundColor
    )

    val opacity by animateFloatAsState(if(enabled) 1f else .65f, spring(1f, 500f))

    CompositionLocalProvider(LocalContentColor provides contentColor) {
        AtomicButton(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            designModifier = Modifier
                .clip(shape)
                .graphicsLayer { alpha = opacity }
                .then(if(border != null) Modifier.border(border, shape) else Modifier)
                .drawBehind { drawRect(color) },
            contentPadding = contentPadding,
            textStyle = textStyle,
            indication = indication,
            horizontalArrangement = horizontalArrangement,
            verticalAlignment = verticalAlignment,
            interactionSource =  interactionSource,
            minWidth = minWidth,
            minHeight = minHeight,
            content = content
        )
    }
}