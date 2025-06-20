package com.somnionocte.atomic_components.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.somnionocte.atomic_components.core.LocalContentColor
import com.somnionocte.atomic_components.extensions.fixedClickable

@Composable
fun AtomicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    designModifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = LocalIndication.current,
    enabled: Boolean = true,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(12.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    contentColor: Color = LocalContentColor.current,
    minSize: DpSize = DpSize(24.dp, 24.dp),
    content: @Composable RowScope.() -> Unit
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier
                .defaultMinSize(minSize.width, minSize.height)
                .fixedClickable(
                    onClick = onClick,
                    enabled = enabled,
                    indication = indication,
                    interactionSource = interactionSource,
                    role = Role.Button
                )
                .then(designModifier),
            propagateMinConstraints = true
        ) {
            Row(
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = verticalAlignment
            ) {
                content()
            }
        }
    }
}