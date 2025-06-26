package com.somnionocte.atomic_components.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun AtomicSlider(
    value: Float,
    onChange: (value: Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    thumb: @Composable (value: Float) -> Unit = {

    },
    track: @Composable (value: Float) -> Unit = {

    }
) {
    Box(modifier) {
        track(value)
        thumb(value)
    }
}