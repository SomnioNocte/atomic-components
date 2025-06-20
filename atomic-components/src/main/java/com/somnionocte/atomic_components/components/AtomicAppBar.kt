package com.somnionocte.atomic_components.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun AtomicAppBar(
    modifier: Modifier = Modifier,
    designModifier: Modifier = Modifier.statusBarsPadding(),
    start: (@Composable RowScope.() -> Unit)? = null,
    end: (@Composable RowScope.() -> Unit)? = null,
    center: (@Composable RowScope.() -> Unit)? = null,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(0.dp),
    textStyle: TextStyle = LocalTextStyle.current,
) {
    ProvideTextStyle(textStyle) {
        Box(modifier.fillMaxWidth().then(designModifier)) {
            if(center != null) Row(
                Modifier.align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = horizontalArrangement,
                content = center
            )

            if(start != null) Row(
                Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = horizontalArrangement,
                content = start
            )

            if(end != null) Row(
                Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = horizontalArrangement,
                content = end
            )
        }
    }
}