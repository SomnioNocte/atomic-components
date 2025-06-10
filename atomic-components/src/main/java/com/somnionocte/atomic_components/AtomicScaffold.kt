package com.somnionocte.atomic_components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxOfOrDefault
import com.somnionocte.atomic_components.core.LazyScrollBehavior
import com.somnionocte.atomic_components.core.LocalContentColor
import com.somnionocte.atomic_components.core.LocalScrollBehavior
import com.somnionocte.atomic_components.core.NestedScrollBehavior
import com.somnionocte.atomic_components.core.NullScrollBehavior
import com.somnionocte.atomic_components.core.ScrollBehavior
import com.somnionocte.atomic_components.extensions.plus

@Composable
fun AtomicScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = { Spacer(Modifier.statusBarsPadding()) },
    bottomBar: @Composable () -> Unit = { Spacer(Modifier.navigationBarsPadding()) },
    contentPadding: PaddingValues = PaddingValues(0.dp),
    containerColor: Color = Color.Unspecified,
    contentColor: Color = LocalContentColor.current,
    scrollBehavior: ScrollBehavior? = null,
    content: @Composable (innerPadding: PaddingValues) -> Unit
) {
    val scrollBehavior = remember(scrollBehavior) { scrollBehavior ?: NullScrollBehavior }

    val displayCutout = WindowInsets.displayCutout

    AtomicSurface(
        modifier
            .imePadding()
            .then(
                if(scrollBehavior is LazyScrollBehavior)
                    Modifier.nestedScroll(NestedScrollBehavior(scrollBehavior))
                else
                    Modifier
            ),
        color = containerColor,
        contentColor = contentColor
    ) {
        SubcomposeLayout { constrains ->
            val constrains = constrains.copy(minWidth = 0, minHeight = 0)

            val topPlaceables = subcompose("top") {
                CompositionLocalProvider(LocalScrollBehavior provides scrollBehavior, topBar)
            }.fastMap { it.measure(constrains) }

            val topBarPadding = PaddingValues(top = topPlaceables.fastMaxOfOrDefault(0) { it.height }.toDp())

            scrollBehavior.topBarHeight = topBarPadding.calculateTopPadding().roundToPx()

            val bottomPlaceables = subcompose("bottom") {
                CompositionLocalProvider(LocalScrollBehavior provides scrollBehavior, bottomBar)
            }.fastMap { it.measure(constrains) }

            val bottomBarPadding = PaddingValues(bottom = bottomPlaceables.fastMaxOfOrDefault(0) { it.height }.toDp())

            scrollBehavior.bottomBarHeight = bottomBarPadding.calculateBottomPadding().roundToPx()

            val sidePaddings = PaddingValues(
                start = displayCutout.getLeft(Density(density), layoutDirection).toDp(),
                end = displayCutout.getRight(Density(density), layoutDirection).toDp()
            )

            val contentPlaceables = subcompose("content") { content(contentPadding + bottomBarPadding + topBarPadding + sidePaddings) }
                .fastMap { it.measure(constrains) }

            layout(constrains.maxWidth, constrains.maxHeight) {
                contentPlaceables.fastForEach {
                    it.placeRelative(IntOffset.Zero)
                }

                topPlaceables.fastForEach {
                    it.placeRelative(IntOffset.Zero)
                }

                bottomPlaceables.fastForEach {
                    it.placeRelative(IntOffset(
                        x = 0,
                        y = constrains.maxHeight - it.height
                    ))
                }
            }
        }
    }
}