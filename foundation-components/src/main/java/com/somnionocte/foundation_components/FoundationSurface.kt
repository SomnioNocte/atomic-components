package com.somnionocte.foundation_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Indication
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.isContainer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Density
import com.somnionocte.foundation_components.core.LocalContentColor
import com.somnionocte.foundation_components.extensions.clearFocusOnTap
import com.somnionocte.foundation_components.extensions.fixedClickable
import com.somnionocte.foundation_components.extensions.surface

@Composable
fun FoundationSurface(
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
fun FoundationSurface(
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    contentColor: Color = LocalContentColor.current,
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    clearFocusOnTap: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    val brush = remember(color) { SolidColor(color) }

    FoundationSurface(
        modifier = modifier,
        designModifier = Modifier.surface(brush, shape, border),
        contentColor = contentColor,
        clearFocusOnTap = clearFocusOnTap,
        content = content
    )
}

@Composable
fun FoundationSurface(
    modifier: Modifier = Modifier,
    surfaceColor: Brush,
    contentColor: Color = LocalContentColor.current,
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    clearFocusOnTap: Boolean = true,
    content: @Composable BoxScope.() -> Unit
) {
    FoundationSurface(
        modifier = modifier,
        designModifier = Modifier.surface(surfaceColor, shape, border),
        contentColor = contentColor,
        clearFocusOnTap = clearFocusOnTap,
        content = content
    )
}