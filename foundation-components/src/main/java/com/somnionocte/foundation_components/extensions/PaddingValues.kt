package com.somnionocte.foundation_components.extensions

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection

operator fun PaddingValues.plus(other: PaddingValues): PaddingValues {
    val ltr = LayoutDirection.Ltr
    return PaddingValues(
        calculateStartPadding(ltr) + other.calculateStartPadding(ltr),
        calculateTopPadding() + other.calculateTopPadding(),
        calculateEndPadding(ltr) + other.calculateEndPadding(ltr),
        calculateBottomPadding() + other.calculateBottomPadding()
    )
}