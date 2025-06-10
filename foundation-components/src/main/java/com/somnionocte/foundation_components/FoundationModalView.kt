package com.somnionocte.foundation_components

import android.provider.SyncStateContract.Columns
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.currentCompositeKeyHash
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.somnio_nocte.overscroll.delegateOverscroll
import com.somnio_nocte.portal.BasicModalView
import com.somnio_nocte.portal.Portal
import com.somnionocte.foundation_components.core.LocalContentColor

@Composable
fun FoundationModalView(
    onRequestDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    key: Any = currentCompositeKeyHash,
    containerColor: Color = Color.Unspecified,
    contentColor: Color = LocalContentColor.current,
    backgroundColor: Color = Color.Black.copy(.3f),
    shape: Shape = RectangleShape,
    border: BorderStroke? = null,
    opacitySpec: (Boolean) -> FiniteAnimationSpec<Float> = { spring(1f, 300f) },
    offsetSpec: (Boolean) -> FiniteAnimationSpec<Float> = { spring(1f, 400f) },
    scrollState: ScrollState = rememberScrollState(),
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Portal(key) { transition ->
        val opacity by transition.animateFloat(
            transitionSpec = { opacitySpec(transition.targetState) },
            targetValueByState = { if(it) 1f else 0f }
        )

        val offset by transition.animateFloat(
            transitionSpec = { offsetSpec(transition.targetState) },
            targetValueByState = { if(it) 0f else 10f }
        )

        Box(Modifier
            .fillMaxSize()
            .drawBehind {
                if(backgroundColor.alpha != 0f) drawRect(backgroundColor.copy(backgroundColor.alpha * opacity))
            }
                then
                if(transition.targetState)
                    Modifier.pointerInput(Unit) { detectTapGestures(onTap = { onRequestDismiss() }) }
                else
                    Modifier,
            contentAlignment = Alignment.BottomCenter
        ) {
            val overscroll = remember { Animatable(0f) }

            CompositionLocalProvider(LocalContentColor provides contentColor) {
                Column(modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .graphicsLayer {
                        translationY = offset * EaseOutCubic.transform(.1f * size.height)
                        if(overscroll.value > 0f)
                            translationY += overscroll.value
                    }
                    .then(
                        if(border != null) Modifier.border(border)
                        else Modifier
                    )
                    .drawBehind { if(containerColor.alpha != 0f) drawRect(containerColor) }
                    .graphicsLayer {
                        if(shape != RectangleShape) {
                            this.shape = shape
                            clip = true
                        }
                    }
                    .pointerInput(Unit) { detectTapGestures() }
                    .delegateOverscroll(scrollState, overscroll, onGestureDown = onRequestDismiss)
                    .verticalScroll(scrollState)
                    .padding(contentPadding)
                ) {
                    content()
                }
            }
        }
    }
}