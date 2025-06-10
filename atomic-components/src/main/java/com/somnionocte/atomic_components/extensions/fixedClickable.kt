package com.somnionocte.atomic_components.extensions

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role

/**
 * Temporary solution to the noticeable delay when tapping on scrolling content.
 * In case of an official fix, can easily be replaced with the standard clickable (arguments to you and in the same order).
 * */
fun Modifier.fixedClickable(
    interactionSource: MutableInteractionSource?,
    indication: Indication?,
    enabled: Boolean = true,
    onClickLabel: String? = null,
    role: Role? = null,
    onClick: () -> Unit
) = composed {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    clickable(
        interactionSource = interactionSource,
        indication = indication,
        enabled = enabled,
        onClick = onClick,
        role = role,
        onClickLabel = onClickLabel
    ).pointerInput(enabled) {
        if(enabled) {
            detectTapGestures(
                onTap = { onClick() },
                onPress = { offset ->
                    val press = PressInteraction.Press(offset)
                    interactionSource.emit(press)
                    tryAwaitRelease()
                    interactionSource.emit(PressInteraction.Release(press))
                }
            )
        }
    }
}