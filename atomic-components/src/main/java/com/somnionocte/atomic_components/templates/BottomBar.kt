package com.somnionocte.atomic_components.templates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.somnionocte.atomic_components.AtomicSurface
import com.somnionocte.atomic_components.core.LocalContentColor
import com.somnionocte.atomic_components.core.LocalScrollBehavior

@Composable
fun TemplateBottomBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = LocalContentColor.current,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable RowScope.() -> Unit
) {
    AtomicSurface(
        modifier = modifier,
        designModifier = Modifier.drawBehind { drawRect(backgroundColor) },
        contentColor = contentColor
    ) {
        Row(
            Modifier.padding(contentPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontalArrangement,
            content = content
        )
    }
}

@Composable
fun TemplateCollapseBottomBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = LocalContentColor.current,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.SpaceEvenly,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable RowScope.() -> Unit
) {
    val collapseProgress by LocalScrollBehavior.current.latestScrollProgress.collectAsStateWithLifecycle(0f)

    AtomicSurface(
        modifier = modifier.graphicsLayer { translationY = size.height * collapseProgress },
        designModifier = Modifier.drawBehind { drawRect(backgroundColor) },
        contentColor = contentColor
    ) {
        Row(
            Modifier.padding(contentPadding).navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontalArrangement,
            content = content
        )
    }
}