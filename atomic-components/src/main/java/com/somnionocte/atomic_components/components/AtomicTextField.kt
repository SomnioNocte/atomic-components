package com.somnionocte.atomic_components.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.isSpecified
import com.somnionocte.atomic_components.core.LocalContentColor

@Composable
private fun DecorationBox(
    isEmpty: () -> Boolean,
    designModifier: Modifier = Modifier,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    support: (@Composable () -> Unit)? = null,
    innerTextFieldGap: Dp = 16.dp,
    minWidth: Dp = 200.dp,
    maxWidth: Dp = Dp.Unspecified,
    innerTextField: @Composable () -> Unit
) {
    Column {
        label?.invoke()

        val prefix = remember(prefix) { movableContentOf {
            prefix?.also { Box(Modifier.padding(end = innerTextFieldGap)) { it() } }
        } }

        val placeholder = remember(prefix) { movableContentOf {
            placeholder?.also { Box { it() } }
        } }

        val suffix = remember(prefix) { movableContentOf {
            suffix?.also { Box(Modifier.padding(start = innerTextFieldGap)) { it() } }
        } }

        Layout(
            listOf({ prefix() }, { placeholder() }, innerTextField, { suffix() }),
            Modifier.widthIn(minWidth, maxWidth) then designModifier
        ) { measurables, constrains ->
            val prefix = measurables[0].firstOrNull()?.measure(constrains.copy(
                minWidth = 0,
                minHeight = 0
            ))
            val suffix = measurables[3].firstOrNull()?.measure(constrains.copy(
                minWidth = 0,
                minHeight = 0
            ))

            val innerTextField = measurables[2].first().measure(constrains.copy(
                minWidth = (constrains.minWidth - (prefix?.measuredWidth ?: 0) - (suffix?.measuredWidth ?: 0)).coerceAtLeast(0),
                maxWidth = (constrains.maxWidth - (prefix?.measuredWidth ?: 0) - (suffix?.measuredWidth ?: 0)).coerceAtLeast(0)
            ))
            val placeholder = measurables[1].firstOrNull()?.measure(constrains.copy(
                minWidth = (constrains.minWidth - (prefix?.measuredWidth ?: 0) - (suffix?.measuredWidth ?: 0)).coerceAtLeast(0),
                maxWidth = (constrains.maxWidth - (prefix?.measuredWidth ?: 0) - (suffix?.measuredWidth ?: 0)).coerceAtLeast(0)
            ))

            val layoutWidth = innerTextField.width + (prefix?.measuredWidth ?: 0) + (suffix?.measuredWidth ?: 0)
            val layoutHeight = maxOf(innerTextField.height, prefix?.height ?: 0, suffix?.height ?: 0, placeholder?.height ?: 0)

            layout(layoutWidth, layoutHeight) {
                if(isEmpty()) placeholder?.place(IntOffset(
                    x = prefix?.width ?: 0,
                    y = (layoutHeight * .5f - placeholder.height * .5f).toInt()
                ))

                innerTextField.place(IntOffset(
                    x = prefix?.width ?: 0,
                    y = (layoutHeight * .5f - innerTextField.height * .5f).toInt()
                ))

                prefix?.place(IntOffset(
                    x = 0,
                    y = (layoutHeight * .5f - prefix.height * .5f).toInt()
                ))

                suffix?.place(IntOffset(
                    x = layoutWidth - suffix.width,
                    y = (layoutHeight * .5f - suffix.height * .5f).toInt()
                ))
            }
        }

        support?.invoke()
    }
}

@Composable
fun AtomicSecureTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    designModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    support: (@Composable () -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    inputTransformation: InputTransformation? = null,
    interactionSource: MutableInteractionSource? = null,
    minWidth: Dp = 200.dp,
    maxWidth: Dp = Dp.Unspecified,
    innerTextFieldGap: Dp = 16.dp,
    textColor: Color = LocalContentColor.current,
    cursorColor: Color = textColor,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val textColor = textStyle.color.takeOrElse { textColor }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    BasicSecureTextField(
        state = state,
        modifier = modifier,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(cursorColor),
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        interactionSource = interactionSource,
        inputTransformation = inputTransformation,
        decorator = { innerTextField ->
            ProvideTextStyle(mergedTextStyle) {
                DecorationBox(
                    isEmpty = { state.text.isEmpty() },
                    designModifier = designModifier,
                    label = label,
                    placeholder = placeholder,
                    prefix = prefix,
                    suffix = suffix,
                    support = support,
                    innerTextField = innerTextField,
                    innerTextFieldGap = innerTextFieldGap,
                    minWidth = minWidth,
                    maxWidth = maxWidth
                )
            }
        }
    )
}

@Composable
fun AtomicTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    designModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    support: (@Composable () -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.Default,
    inputTransformation: InputTransformation? = null,
    outputTransformation: OutputTransformation? = null,
    scrollState: ScrollState = rememberScrollState(),
    interactionSource: MutableInteractionSource? = null,
    minWidth: Dp = 200.dp,
    maxWidth: Dp = Dp.Unspecified,
    innerTextFieldGap: Dp = 16.dp,
    textColor: Color = LocalContentColor.current,
    cursorColor: Color = textColor,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val textColor = textStyle.color.takeOrElse { textColor }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    BasicTextField(
        state = state,
        modifier = modifier,
        textStyle = mergedTextStyle,
        // Fixes scrolling when color is transparent or unspecified. Yes scrolling. Yes color was the problem.
        cursorBrush = SolidColor(cursorColor.let { if(it.isSpecified) it.copy(1f) else Color.Black }),
        enabled = enabled,
        readOnly = readOnly,
        keyboardOptions = keyboardOptions,
        onKeyboardAction = onKeyboardAction,
        interactionSource = interactionSource,
        lineLimits = lineLimits,
        inputTransformation = inputTransformation,
        outputTransformation = outputTransformation,
        scrollState = scrollState,
        decorator = { innerTextField ->
            ProvideTextStyle(mergedTextStyle) {
                DecorationBox(
                    isEmpty = { state.text.isEmpty() },
                    designModifier = designModifier,
                    label = label,
                    placeholder = placeholder,
                    prefix = prefix,
                    suffix = suffix,
                    support = support,
                    innerTextField = innerTextField,
                    innerTextFieldGap = innerTextFieldGap,
                    minWidth = minWidth,
                    maxWidth = maxWidth
                )
            }
        }
    )
}

@Composable
fun AtomicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    designModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    label: (@Composable () -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    prefix: (@Composable () -> Unit)? = null,
    suffix: (@Composable () -> Unit)? = null,
    support: (@Composable () -> Unit)? = null,
    textStyle: TextStyle = LocalTextStyle.current,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {  },
    interactionSource: MutableInteractionSource? = null,
    minWidth: Dp = 200.dp,
    maxWidth: Dp = Dp.Unspecified,
    innerTextFieldGap: Dp = 16.dp,
    textColor: Color = LocalContentColor.current,
    cursorColor: Color = textColor,
) {
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val textColor = textStyle.color.takeOrElse { textColor }
    val mergedTextStyle = textStyle.merge(TextStyle(color = textColor))

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = mergedTextStyle,
        cursorBrush = SolidColor(cursorColor),
        enabled = enabled,
        readOnly = readOnly,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        interactionSource = interactionSource,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        onTextLayout = onTextLayout
    ) { innerTextField ->
        ProvideTextStyle(mergedTextStyle) {
            DecorationBox(
                isEmpty = { value.isEmpty() },
                designModifier = designModifier,
                label = label,
                placeholder = placeholder,
                prefix = prefix,
                suffix = suffix,
                support = support,
                innerTextField = innerTextField,
                innerTextFieldGap = innerTextFieldGap,
                minWidth = minWidth,
                maxWidth = maxWidth
            )
        }
    }
}