package com.somnionocte.atomic_components.components

import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

@Composable
fun AtomicToggleable(
    state: ToggleableState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    designModifier: Modifier,
    enabled: Boolean = true,
    minSize: DpSize = DpSize(48.dp, 48.dp),
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = LocalIndication.current,
    role: Role,
    content: @Composable BoxScope.() -> Unit = {  }
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    Box(
        modifier
            .size(minSize)
            .triStateToggleable(state, interactionSource, indication, enabled, role, onClick)
            .pointerInput(state, enabled) {
                if(enabled) detectTapGestures(
                    onTap = { onClick() },
                    onPress = {
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    }
                )
            }
            .then(designModifier),
        content = content
    )
}

@Composable
fun AtomicToggleable(
    state: Boolean,
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    designModifier: Modifier,
    enabled: Boolean = true,
    minSize: DpSize = DpSize(48.dp, 48.dp),
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = LocalIndication.current,
    role: Role,
    content: @Composable BoxScope.() -> Unit = {  }
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

    Box(
        modifier
            .size(minSize)
            .toggleable(state, interactionSource, indication, enabled, role, onClick)
            .pointerInput(state, enabled) {
                if(enabled) detectTapGestures(
                    onTap = { onClick(!state) },
                    onPress = {
                        val press = PressInteraction.Press(it)
                        interactionSource.emit(press)
                        tryAwaitRelease()
                        interactionSource.emit(PressInteraction.Release(press))
                    }
                )
            }
            .then(designModifier),
        content = content
    )
}