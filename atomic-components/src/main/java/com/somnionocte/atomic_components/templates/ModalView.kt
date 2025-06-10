package com.somnionocte.atomic_components.templates

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.somnio_nocte.portal.Portal
import com.somnionocte.atomic_components.core.LocalContentColor

@Composable
fun TemplateFullModalView(
    onRequestDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    key: Any = currentCompositeKeyHash,
    margin: PaddingValues = PaddingValues(0.dp),
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    overlayColor: Color = Color.Black.copy(.35f),
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = LocalContentColor.current,
    animationSpec: FiniteAnimationSpec<Float> = spring(1f, 500f),
    content: @Composable BoxScope.() -> Unit
) {
    Portal(key) { transition ->
        val transitionFraction by transition.animateFloat({ animationSpec }) { if(it) 100f else 0f }

        Box(Modifier
            .fillMaxSize()
            .drawBehind { drawRect(lerp(Color.Transparent, overlayColor, transitionFraction * .01f)) }
            .then(if(transition.targetState) Modifier.pointerInput(Unit) { detectTapGestures(onTap = { onRequestDismiss() }) } else Modifier),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(modifier
                .padding(margin)
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = size.height * (1f - transitionFraction * .01f)
                }
                .then(if(border != null) Modifier.border(border) else Modifier)
                .clip(shape)
                .pointerInput(Unit) { detectTapGestures() }
                .background(backgroundColor)
            ) {
                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    content()
                }
            }
        }
    }
}