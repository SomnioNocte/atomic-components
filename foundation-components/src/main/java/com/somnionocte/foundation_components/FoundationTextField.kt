package com.somnionocte.foundation_components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

enum class FoundationTextFieldLabelPosition {
    Outside,
    Inside,
    InlineBorder
}

@Composable
private fun DecorationBox(
    isEmpty: () -> Boolean,
    labelPosition: FoundationTextFieldLabelPosition,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    innerTextField: @Composable () -> Unit,
    innerTextFieldGap: Dp = 8.dp
) {
    @Composable
    fun MainRow() {
        Layout(
            listOf(
                { Row(verticalAlignment = Alignment.CenterVertically) { prefix?.invoke() } },
                { Box {
                    placeholder?.let { if(isEmpty()) it() }
                    innerTextField()
                } },
                { Row(verticalAlignment = Alignment.CenterVertically) { suffix?.invoke() } },
            )
        ) { measurables, constrains ->
            val constrains = constrains.copy(minWidth = 0, minHeight = 0)

            val gap = innerTextFieldGap.roundToPx()

            val prefixPlaceable =
                if(prefix != null) measurables[0].first().measure(constrains)
                else null

            val prefixWidth = prefixPlaceable?.run { measuredWidth + gap } ?: 0

            val suffixPlaceable =
                if(suffix != null) measurables[2].first().measure(constrains)
                else null

            val suffixWidth = suffixPlaceable?.run { measuredWidth + gap } ?: 0

            val minWidth = 250.dp.roundToPx() - suffixWidth - prefixWidth
            val maxWidth = constrains.maxWidth - suffixWidth - prefixWidth

            val innerTextFieldPlaceable = measurables[1].first().measure(constrains.copy(minWidth = minWidth, maxWidth = maxWidth))

            val width = prefixWidth + innerTextFieldPlaceable.measuredWidth + suffixWidth

            val height = maxOf(
                prefixPlaceable?.measuredHeight ?: 0,
                innerTextFieldPlaceable.measuredHeight,
                suffixPlaceable?.measuredHeight ?: 0
            )

            layout(width, height) {
                prefixPlaceable?.place(IntOffset(
                    0,
                    (height * .5f - prefixPlaceable.measuredHeight * .5f).toInt()
                ))

                innerTextFieldPlaceable.place(IntOffset(
                    prefixWidth,
                    (height * .5f - innerTextFieldPlaceable.measuredHeight * .5f).toInt()
                ))

                suffixPlaceable?.place(IntOffset(
                    width - suffixPlaceable.measuredWidth,
                    (height * .5f - suffixPlaceable.measuredHeight * .5f).toInt()
                ))
            }
        }
    }

    if(label == null || labelPosition == FoundationTextFieldLabelPosition.Outside) {
        MainRow()
    } else if(labelPosition == FoundationTextFieldLabelPosition.Inside) {
        Column(verticalArrangement = Arrangement.spacedBy(innerTextFieldGap)) {
            label()
            MainRow()
        }
    } else {
        Box {
            MainRow()
            label()
        }
    }
}

@Composable
fun FoundationTextField(
    value: String,
    onChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
    labelPosition: FoundationTextFieldLabelPosition = FoundationTextFieldLabelPosition.Inside,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    supportingTextArrangement: Arrangement.Horizontal = Arrangement.SpaceBetween,
    supportingTextModifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource? = null,
    innerTextFieldGap: Dp = 8.dp,
    cursorColor: Color? = textStyle.color
) {
    ProvideTextStyle(textStyle.merge(TextStyle(color = textStyle.color.copy(.7f)))) {
        Layout(listOf(
            { supportingText?.let {
                Row(supportingTextModifier, horizontalArrangement = supportingTextArrangement) { it() }
            } },
            {
                BasicTextField(
                    value,
                    onChange,
                    modifier,
                    readOnly = readOnly,
                    textStyle = textStyle,
                    enabled = enabled,
                    maxLines = maxLines,
                    minLines = minLines,
                    interactionSource = interactionSource,
                    visualTransformation = visualTransformation,
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions,
                    cursorBrush = SolidColor(cursorColor ?: LocalTextStyle.current.color)
                ) { innerTextField ->
                    DecorationBox(
                        { value.isEmpty() },
                        labelPosition,
                        label,
                        placeholder,
                        prefix,
                        suffix,
                        innerTextField,
                        innerTextFieldGap
                    )
                }
            }
        )) { measurables, constrains ->
            val constrains = constrains.copy(minWidth = 0, minHeight = 0)

            val textFieldPlaceable = measurables[1].first().measure(constrains)

            val supportPlaceable =
                if(supportingText != null) measurables.first().first()
                    .measure(constrains.copy(
                        minWidth = textFieldPlaceable.measuredWidth,
                        maxWidth = textFieldPlaceable.measuredWidth
                    ))
                else null

            layout(
                textFieldPlaceable.measuredWidth,
                textFieldPlaceable.measuredHeight + (supportPlaceable?.measuredHeight ?: 0)
            ) {
                textFieldPlaceable.place(IntOffset.Zero)

                supportPlaceable?.place(IntOffset(0, textFieldPlaceable.measuredHeight))
            }
        }
    }
}