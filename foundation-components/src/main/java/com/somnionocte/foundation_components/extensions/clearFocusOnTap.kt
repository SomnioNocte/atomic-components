package com.somnionocte.foundation_components.extensions

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager

fun Modifier.clearFocusOnTap() = composed {
    val focusManager = LocalFocusManager.current
    pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
}