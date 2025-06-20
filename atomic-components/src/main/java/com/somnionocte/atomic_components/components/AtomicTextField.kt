package com.somnionocte.atomic_components.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
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
import com.somnionocte.atomic_components.core.LocalContentColor

@Composable
private fun DecorationBox(
    isEmpty: () -> Boolean,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    innerTextField: @Composable () -> Unit,
    minWidth: Dp = 250.dp,
    minHeight: Dp = 28.dp,
    innerTextFieldGap: Dp = 8.dp
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(innerTextFieldGap)
    ) {
        label?.invoke()

        Row(
            Modifier.defaultMinSize(minWidth, minHeight),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(innerTextFieldGap)
        ) {
            prefix?.invoke()

            if(placeholder == null) {
                innerTextField()
            } else Box(contentAlignment = Alignment.CenterStart) {
                if(isEmpty()) placeholder.invoke()
                innerTextField()
            }

            suffix?.invoke()
        }
    }
}

@Composable
fun AtomicTextField(
    value: String,
    onChange: (value: String) -> Unit,
    modifier: Modifier = Modifier,
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
    minWidth: Dp = 250.dp,
    minHeight: Dp = 24.dp,
    cursorColor: Color? = textStyle.color
) {
    val textStyle = LocalTextStyle.current.merge(textStyle)

    ProvideTextStyle(textStyle) {
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
                    singleLine = singleLine,
                    interactionSource = interactionSource,
                    visualTransformation = visualTransformation,
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions,
                    cursorBrush = SolidColor(cursorColor ?: LocalTextStyle.current.color)
                ) { innerTextField ->
                    DecorationBox(
                        { value.isEmpty() },
                        label,
                        placeholder,
                        prefix,
                        suffix,
                        innerTextField,
                        minWidth,
                        minHeight,
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