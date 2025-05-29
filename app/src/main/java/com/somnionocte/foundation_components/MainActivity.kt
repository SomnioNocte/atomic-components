package com.somnionocte.foundation_components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.somnionocte.foundation_components.core.ProvideTextStyle
import com.somnionocte.foundation_components.core.ScaleIndication
import com.somnionocte.foundation_components.core.mix
import com.somnionocte.foundation_components.ui.theme.FoundationComponentsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FoundationComponentsTheme {
                CompositionLocalProvider(LocalIndication provides ScaleIndication) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        val scrollState = rememberScrollState()

                        Column(
                            Modifier
                                .fillMaxSize()
                                .imePadding()
                                .coreScrollbar(scrollState, contentPadding = PaddingValues(8.dp, 16.dp))
                                .verticalScroll(scrollState)
                                .padding(innerPadding)
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Spacer(Modifier.height(128.dp))

                            MaterialYou.FilledButton({ }) {
                                Text(
                                    "Basic button",
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }

                            var value by remember { mutableStateOf("") }

                            MaterialYou.TextField(
                                value, { value = it },
                                placeholder = { com.somnionocte.foundation_components.core.Text("placeholder") },
                                isError = value == "error",
                                supportingText = {
                                    Text("some")
                                    Text("-/-")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

object MaterialYou {
    @Composable
    fun FilledButton(
        onClick: () -> Unit,
        enabled: Boolean = true,
        content: @Composable RowScope.() -> Unit
    ) {
        val backgroundColor = MaterialTheme.colorScheme.primaryContainer
        val contentColor = MaterialTheme.colorScheme.onPrimaryContainer

        AnimatedFoundationButton(
            onClick = onClick,
            enabled = enabled,
            contentPadding = PaddingValues(18.dp, 8.dp),
            backgroundColor = backgroundColor,
            shape = RoundedCornerShape(1000.dp),
            pressedBackgroundColor = remember { backgroundColor.mix(.125f, contentColor) },
            contentColor = contentColor,
            content = content
        )
    }

    @Composable
    fun TextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        readOnly: Boolean = false,
        label: @Composable() (() -> Unit)? = null,
        placeholder: @Composable() (() -> Unit)? = null,
        prefix: @Composable() (() -> Unit)? = null,
        suffix: @Composable() (() -> Unit)? = null,
        supportingText: @Composable() (() -> Unit)? = null,
        isError: Boolean = false,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        singleLine: Boolean = false,
        maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
        minLines: Int = 1,
        interactionSource: MutableInteractionSource? = null,
    ) {
        val interactionSource = interactionSource ?: remember { MutableInteractionSource() }

        val isFocused by interactionSource.collectIsFocusedAsState()

        val accent = MaterialTheme.colorScheme.run { if(isError) error else primary }

        FoundationTextField(
            value,
            onValueChange,
            modifier
                .border(4.dp, accent.copy(if(isFocused) .45f else 0f), RoundedCornerShape(22.dp))
                .padding(3.dp)
                .border(2.dp, accent.copy(if(isFocused) 1f else 0f), RoundedCornerShape(19.dp))
                .padding(3.dp)
                .background(
                    MaterialTheme.colorScheme.surfaceContainerLowest,
                    RoundedCornerShape(16.dp)
                )
                .padding(14.dp),
            interactionSource = interactionSource,
            textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp),
            enabled = enabled,
            readOnly = readOnly,
            maxLines = maxLines,
            minLines = minLines,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            label = label?.let { {
                ProvideTextStyle(TextStyle(color = accent.copy(.8f)), it)
            } },
            placeholder = placeholder,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            supportingTextModifier = Modifier.padding(16.dp, 3.dp),
            cursorColor = accent
        )
    }
}