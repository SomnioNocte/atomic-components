package com.somnionocte.atomic_components.templates

import androidx.compose.animation.core.EaseIn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.somnionocte.atomic_components.AtomicAppBar
import com.somnionocte.atomic_components.AtomicSurface
import com.somnionocte.atomic_components.core.LocalContentColor
import com.somnionocte.atomic_components.core.LocalScrollBehavior
import com.somnionocte.atomic_components.extensions.mix

@Composable
fun TemplatePinnedAppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = LocalContentColor.current,
    header: @Composable () -> Unit = {  },
    navigation: @Composable () -> Unit = {  },
    alignHeaderToCenter: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    actions: @Composable () -> Unit = {  }
) {
    AtomicSurface(
        modifier
            .drawBehind { drawRect(backgroundColor) }
            .statusBarsPadding()
            .padding(contentPadding),
        contentColor = contentColor
    ) {
        val alignedHeader = remember(header) { movableContentOf {
            Row(verticalAlignment = Alignment.CenterVertically) { header() }
        } }

        AtomicAppBar(
            textStyle = remember { TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp) },
            start = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    navigation()

                    if(!alignHeaderToCenter) alignedHeader()
                }
            },
            center = {
                if(alignHeaderToCenter) alignedHeader()
            },
            end = {
                actions()
            }
        )
    }
}

@Composable
fun TemplatePinnedAppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = LocalContentColor.current,
    header: @Composable () -> Unit = {  },
    navigation: @Composable () -> Unit = {  },
    inactiveBackgroundColor: Color,
    alignHeaderToCenter: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    actions: @Composable () -> Unit = {  }
) {
    val topProgress by LocalScrollBehavior.current.topBoundScrollProgress.collectAsStateWithLifecycle(1f)

    AtomicSurface(
        modifier
            .drawBehind { drawRect(inactiveBackgroundColor.mix(topProgress, backgroundColor)) }
            .statusBarsPadding()
            .padding(contentPadding),
        contentColor = contentColor
    ) {
        val alignedHeader = remember(header) { movableContentOf {
            Row(verticalAlignment = Alignment.CenterVertically) { header() }
        } }

        AtomicAppBar(
            textStyle = remember { TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp) },
            start = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    navigation()

                    if(!alignHeaderToCenter) alignedHeader()
                }
            },
            center = {
                if(alignHeaderToCenter) alignedHeader()
            },
            end = {
                actions()
            }
        )
    }
}

@Composable
fun TemplateCollapseAppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = LocalContentColor.current,
    header: @Composable () -> Unit = {  },
    navigation: @Composable () -> Unit = {  },
    alignHeaderToCenter: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    actions: @Composable () -> Unit = {  }
) {
    val collapseProgress by LocalScrollBehavior.current.latestScrollProgress.collectAsStateWithLifecycle(0f)

    AtomicSurface(
        modifier
            .graphicsLayer { translationY = -size.height * collapseProgress }
            .drawBehind { drawRect(backgroundColor) }
            .statusBarsPadding()
            .padding(contentPadding),
        contentColor = contentColor
    ) {
        val alignedHeader = remember(header) { movableContentOf {
            Row(verticalAlignment = Alignment.CenterVertically) { header() }
        } }

        AtomicAppBar(
            textStyle = remember { TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp) },
            start = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    navigation()

                    if(!alignHeaderToCenter) alignedHeader()
                }
            },
            center = {
                if(alignHeaderToCenter) alignedHeader()
            },
            end = {
                actions()
            }
        )
    }
}

@Composable
fun TemplateLargeAppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    contentColor: Color = LocalContentColor.current,
    largeHeader: @Composable () -> Unit,
    header: @Composable () -> Unit = {  },
    navigation: @Composable () -> Unit = {  },
    bottomPadding: Dp = 48.dp,
    inactiveBackgroundColor: Color = backgroundColor,
    alignHeaderToCenter: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    actions: @Composable () -> Unit = {  }
) {
    val threshold = LocalScrollBehavior.current.topBound
    val topProgress by LocalScrollBehavior.current.topBoundScrollProgress.collectAsStateWithLifecycle(1f)

    AtomicSurface(
        modifier
            .padding(bottom = bottomPadding)
            .drawBehind { drawRect(inactiveBackgroundColor.mix(topProgress, backgroundColor)) }
            .statusBarsPadding()
            .padding(contentPadding),
        contentColor = contentColor
    ) {
        val alignedHeader = remember(header) { movableContentOf {
            Row(
                Modifier.graphicsLayer {
                    alpha = EaseIn.transform((topProgress.coerceIn(0f..5f) - .5f) * 2f)
                    translationY = lerp(threshold.toFloat(), 0f, topProgress)
                } ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                header()
            }
        } }

        Row(
            Modifier
                .graphicsLayer {
                    alpha = EaseIn.transform(1f - (topProgress.coerceIn(0f..5f)) * 2f)
                    translationY = bottomPadding.roundToPx() +
                            contentPadding.calculateTopPadding().roundToPx() +
                            contentPadding.calculateBottomPadding().roundToPx() +
                            lerp(0f, -threshold.toFloat(), topProgress)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            largeHeader()
        }

        AtomicAppBar(
            textStyle = remember { TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 24.sp) },
            start = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    navigation()

                    if(!alignHeaderToCenter) alignedHeader()
                }
            },
            center = {
                if(alignHeaderToCenter) alignedHeader()
            },
            end = {
                actions()
            }
        )
    }
}