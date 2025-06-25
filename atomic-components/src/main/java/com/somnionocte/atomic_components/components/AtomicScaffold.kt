package com.somnionocte.atomic_components.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.somnionocte.compose_extensions.plus

@Composable
fun AtomicScaffold(
    modifier: Modifier = Modifier,
    top: (@Composable () -> Unit)? = null,
    bottom: (@Composable () -> Unit)? = null,
    subBottom: (@Composable () -> Unit)? = null,
    includeSystemPadding: Boolean = true,
    topModifier: Modifier = Modifier,
    bottomModifier: Modifier = Modifier,
    subBottomModifier: Modifier = Modifier,
    contentModifier: Modifier = Modifier,
    content: @Composable (innerPadding: PaddingValues) -> Unit,
) {
    val systemPadding =
        if(includeSystemPadding) WindowInsets.systemBars.asPaddingValues()
        else PaddingValues(0.dp)

    SubcomposeLayout(modifier) { constrains ->
        val constrains = constrains.copy(minWidth = 0, minHeight = 0)

        val topPlaceable =
            if(top != null) subcompose("top") { Box(topModifier) { top() } }.first().measure(constrains)
            else null

        val bottomPlaceable =
            if(bottom != null) subcompose("bottom") { Box(bottomModifier) { bottom() } }.first().measure(constrains)
            else null

        val subBottomPlaceable =
            if(subBottom != null) subcompose("subBottom") { Box(subBottomModifier) { subBottom() } }.first().measure(constrains)
            else null

        val bottomPadding = bottomPlaceable?.height?.toDp() ?: systemPadding.calculateBottomPadding()
        val subBottomPadding = subBottomPlaceable?.height?.toDp() ?: 0.dp

        val finalInnerPadding = PaddingValues(
            top = topPlaceable?.height?.toDp() ?: systemPadding.calculateTopPadding(),
            bottom = bottomPadding + subBottomPadding,
        )

        val contentPlaceable = subcompose("content") { Box(contentModifier) { content(finalInnerPadding) } }
            .first().measure(constrains)

        layout(constrains.maxWidth, constrains.maxHeight) {
            contentPlaceable.place(IntOffset.Zero)

            topPlaceable?.apply {
                place(IntOffset(
                    x = (constrains.maxWidth * .5f - width * .5f).toInt(),
                    y = 0
                ))
            }

            bottomPlaceable?.apply {
                place(IntOffset(
                    x = (constrains.maxWidth * .5f - width * .5f).toInt(),
                    y = (constrains.maxHeight - height).toInt(),
                ))
            }
        }
    }
}