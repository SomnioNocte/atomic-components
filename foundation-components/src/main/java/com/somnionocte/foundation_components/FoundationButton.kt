package com.somnionocte.foundation_components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.somnionocte.foundation_components.core.LocalContentColor
import com.somnionocte.foundation_components.core.LocalTextStyle
import com.somnionocte.foundation_components.core.fixedClickable
import com.somnionocte.foundation_components.core.onPress
import kotlinx.coroutines.launch

/**
 * The button framework, without styles and MaterialDesign dependencies, is used to
 * create stylized buttons for custom design systems.
 * It looks like a pure HTML button with Zero-CSS.
 *
 * @param modifier the modifier to apply to this layout.
 * @param contentPadding a padding around the whole content. This will add padding for the. content
 *   after it has been clipped, which is not possible via [modifier] param. If you want to add a
 *   spacing between each item use [horizontalArrangement].
 * @param verticalAlignment the vertical alignment applied to the items.
 * @param content a block which describes the content.
 */
@Composable
fun AnimatedFoundationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = Color(150, 150, 150),
    pressedBackgroundColor: Color = backgroundColor.copy(.75f),
    disabledBackgroundColor: Color = backgroundColor.copy(.6f),
    contentColor: Color = Color.Black,
    specIn: AnimationSpec<Color> = spring(1f, 3500f),
    specOut: AnimationSpec<Color> = spring(1f, 500f),
    border: BorderStroke? = null,
    minWidth: Dp = 48.dp,
    minHeight: Dp = 48.dp,
    shape: Shape = RectangleShape,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    textStyle: TextStyle = LocalTextStyle.current,
    indication: Indication? = LocalIndication.current,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val color = remember { Animatable(backgroundColor) }

    interactionSource.onPress { awaitRelease ->
        color.animateTo(pressedBackgroundColor, specIn)
        awaitRelease()
        if(enabled) color.animateTo(backgroundColor, specOut)
        else color.animateTo(disabledBackgroundColor, specOut)
    }

    LaunchedEffect(enabled) {
        if(enabled)
            launch { color.animateTo(backgroundColor, specOut) }
        else
            launch { color.animateTo(disabledBackgroundColor, specOut) }
    }

    Box(
        modifier
            .fixedClickable(
                interactionSource = interactionSource,
                indication = indication,
                enabled = enabled,
                onClick = onClick,
                role = Role.Button
            )
            .clip(shape)
            .drawBehind { drawRect(color.value) }
            .then(
                if(border != null) Modifier.border(border, shape)
                else Modifier
            )
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides textStyle
        ) {
            Row(
                Modifier
                    .defaultMinSize(
                        minWidth = minWidth,
                        minHeight = minHeight
                    )
                    .padding(contentPadding),
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = verticalAlignment
            ) {
                content()
            }
        }
    }
}

/**
 * The button framework, without styles and MaterialDesign dependencies, is used to
 * create stylized buttons for custom design systems.
 * It looks like a pure HTML button with Zero-CSS.
 *
 * @param modifier the modifier to apply to this layout.
 * @param contentPadding a padding around the whole content. This will add padding for the. content
 *   after it has been clipped, which is not possible via [modifier] param. If you want to add a
 *   spacing between each item use [horizontalArrangement].
 * @param verticalAlignment the vertical alignment applied to the items.
 * @param content a block which describes the content.
 */
@Composable
fun FoundationButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minWidth: Dp = 48.dp,
    minHeight: Dp = 48.dp,
    designModifier: Modifier = Modifier,
    contentColor: Color = Color.Black,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    textStyle: TextStyle = LocalTextStyle.current,
    indication: Indication? = LocalIndication.current,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    Box(
        modifier
            .fixedClickable(
                interactionSource = interactionSource,
                indication = indication,
                enabled = enabled,
                onClick = onClick,
                role = Role.Button
            )
            .then(designModifier)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides textStyle
        ) {
            Row(
                Modifier
                    .defaultMinSize(
                        minWidth = minWidth,
                        minHeight = minHeight
                    )
                    .padding(contentPadding),
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = verticalAlignment
            ) {
                content()
            }
        }
    }
}