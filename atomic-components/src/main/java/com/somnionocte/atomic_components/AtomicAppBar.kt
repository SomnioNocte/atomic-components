package com.somnionocte.atomic_components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxOfOrDefault

@Composable
fun AtomicAppBar(
    modifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    start: @Composable () -> Unit = {  },
    end: @Composable () -> Unit = {  },
    center: @Composable () -> Unit = {  },
) {
    ProvideTextStyle(textStyle) {
        Layout(
            listOf(start, center, end),
            modifier
        ) { (startMeasurable, centerMeasurable, endMeasurable), constrains ->
            val constrains = constrains.copy(minWidth = 0, minHeight = 0)

            val startPlaceable = startMeasurable.fastMap { it.measure(constrains) }
            val centerPlaceable = centerMeasurable.fastMap { it.measure(constrains) }
            val endPlaceable = endMeasurable.fastMap { it.measure(constrains) }

            val layoutHeight = maxOf(
                startPlaceable.fastMaxOfOrDefault(0) { it.height },
                centerPlaceable.fastMaxOfOrDefault(0) { it.height },
                endPlaceable.fastMaxOfOrDefault(0) { it.height },
            )

            layout(constrains.maxWidth, layoutHeight) {
                centerPlaceable.fastForEach {
                    it.placeRelative(IntOffset(
                        x = (constrains.maxWidth * .5f - it.width * .5f).toInt(),
                        y = (layoutHeight * .5f - it.height * .5f).toInt()
                    ))
                }

                startPlaceable.fastForEach {
                    it.placeRelative(IntOffset(
                        x = 0,
                        y = (layoutHeight * .5f - it.height * .5f).toInt()
                    ))
                }

                endPlaceable.fastForEach {
                    it.placeRelative(IntOffset(
                        x = (constrains.maxWidth - it.width).toInt(),
                        y = (layoutHeight * .5f - it.height * .5f).toInt()
                    ))
                }
            }
        }
    }
}