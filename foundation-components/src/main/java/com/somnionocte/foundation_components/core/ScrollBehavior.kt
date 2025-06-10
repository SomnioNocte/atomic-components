package com.somnionocte.foundation_components.core

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

open class ScrollBehavior {
    open val scrollState: ScrollableState = ScrollState(0)
    open val topBoundScrollProgress: Flow<Float> = flow {  }
    open val bottomBoundScrollProgress: Flow<Float> = flow {  }
    open val latestScrollProgress: Flow<Float> = flow {  }

    var topBarHeight: Int = 100
        internal set
    var bottomBarHeight: Int = 100
        internal set

    open val topBound: Int = 100
    open val bottomBound: Int = 100
}

fun ScrollBehavior(
    scrollState: ScrollState,
    threshold: Int? = 100
): ScrollBehavior {
    return object : ScrollBehavior() {
        override val topBound: Int
            get() = threshold ?: topBarHeight

        override val bottomBound: Int
            get() = threshold ?: bottomBarHeight

        override val scrollState = scrollState

        override val topBoundScrollProgress = snapshotFlow { scrollState.value }
            .map { it.coerceIn(0, topBound).toFloat() / topBound }

        override val bottomBoundScrollProgress = snapshotFlow { scrollState.maxValue - scrollState.value }
            .map { it.coerceIn(0, bottomBound).toFloat() / bottomBound }

        override val latestScrollProgress = flow {
            emit(0f)
        }
    }
}

class LazyScrollBehavior(
    override val scrollState: LazyListState,
    private val threshold: Int? = 100
) : ScrollBehavior() {
    override val topBound: Int
        get() = threshold ?: topBarHeight

    override val bottomBound: Int
        get() = threshold ?: bottomBarHeight

    override val topBoundScrollProgress = snapshotFlow {
        scrollState.layoutInfo.visibleItemsInfo.firstOrNull()
            ?.takeIf { it.index == 0 }
            ?.run { 1f - ((topBound + offset).coerceIn(0, topBound).toFloat() / topBound).coerceIn(0f..1f) } ?: 1f
    }

    override val bottomBoundScrollProgress = snapshotFlow {
        scrollState.layoutInfo.visibleItemsInfo.lastOrNull()
            ?.takeIf { it.index == scrollState.layoutInfo.totalItemsCount - 1 }
            ?.run {
                val relativeOffset = (scrollState.layoutInfo.viewportEndOffset - offset - size - scrollState.layoutInfo.afterContentPadding + bottomBarHeight)
                    .coerceIn(0, bottomBound)
                ((bottomBound - relativeOffset).coerceIn(0, bottomBound).toFloat() / bottomBound).coerceIn(0f..1f)
            } ?: 1f
    }

    internal val latestScrollProgressState = MutableStateFlow(0f)

    override val latestScrollProgress = latestScrollProgressState
}

class NestedScrollBehavior(
    private val scrollBehavior: LazyScrollBehavior
) : NestedScrollConnection {
    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        scrollBehavior.latestScrollProgressState.update {
            (it - available.y / scrollBehavior.topBarHeight).coerceIn(0f, 1f)
        }
        return Offset.Zero
    }
}

@Composable
fun rememberScrollBehavior(
    scrollState: ScrollState,
    threshold: Int? = 100
) = remember(scrollState, threshold) { ScrollBehavior(scrollState, threshold) }

@Composable
fun rememberScrollBehavior(
    scrollState: LazyListState,
    threshold: Int? = 100
) = remember(scrollState, threshold) { LazyScrollBehavior(scrollState, threshold) }

fun ScrollBehavior(scrollState: LazyListState, threshold: Int? = 100) = LazyScrollBehavior(scrollState, threshold)

val NullScrollBehavior = ScrollBehavior(ScrollState(0))

val LocalScrollBehavior = compositionLocalOf<ScrollBehavior> { NullScrollBehavior }