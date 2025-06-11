package com.somnionocte.atomic_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import com.somnionocte.atomic_components.core.LocalContentColor
import com.somnionocte.atomic_components.extensions.clearFocusOnTap
import com.somnionocte.atomic_components.extensions.surface

@Composable
fun AtomicSurface(
    modifier: Modifier = Modifier,
    contentColor: Color = LocalContentColor.current,
    designModifier: Modifier = Modifier,
    clearFocusOnTap: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        Box(
            modifier = modifier
                .semantics { isContainer = true }
                .then(
                    if(clearFocusOnTap) Modifier.clearFocusOnTap()
                    else Modifier.pointerInput(Unit) { }
                )
                .then(designModifier),
            propagateMinConstraints = true,
            content = content
        )
    }
}

@Composable
fun AtomicSurface(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    contentColor: Color = LocalContentColor.current,
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    clearFocusOnTap: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val brush = remember(color) { SolidColor(color) }

    AtomicSurface(
        modifier = modifier,
        designModifier = Modifier.surface(brush, shape, border),
        contentColor = contentColor,
        clearFocusOnTap = clearFocusOnTap,
        content = content
    )
}

@Composable
fun AtomicSurface(
    modifier: Modifier = Modifier,
    surfaceColor: Brush,
    contentColor: Color = LocalContentColor.current,
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    clearFocusOnTap: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    AtomicSurface(
        modifier = modifier,
        designModifier = Modifier.surface(surfaceColor, shape, border),
        contentColor = contentColor,
        clearFocusOnTap = clearFocusOnTap,
        content = content
    )
}