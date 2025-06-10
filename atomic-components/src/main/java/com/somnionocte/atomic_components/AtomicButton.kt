package com.somnionocte.atomic_components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.somnionocte.atomic_components.core.LocalContentColor
import com.somnionocte.atomic_components.extensions.fixedClickable

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
fun AtomicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minWidth: Dp = 48.dp,
    minHeight: Dp = 48.dp,
    designModifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    textStyle: TextStyle = LocalTextStyle.current,
    indication: Indication? = LocalIndication.current,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    interactionSource: MutableInteractionSource? = null,
    content: @Composable RowScope.() -> Unit
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