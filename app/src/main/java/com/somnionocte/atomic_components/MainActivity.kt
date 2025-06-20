package com.somnionocte.atomic_components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.somnio_nocte.portal.NexusPortal
import com.somnionocte.atomic_components.components.AtomicAppBar
import com.somnionocte.atomic_components.components.AtomicButton
import com.somnionocte.atomic_components.components.AtomicScaffold
import com.somnionocte.atomic_components.components.AtomicSurface
import com.somnionocte.atomic_components.components.AtomicTextField
import com.somnionocte.atomic_components.components.Icon
import com.somnionocte.atomic_components.components.LocalTextStyle
import com.somnionocte.atomic_components.components.Text
import com.somnionocte.atomic_components.core.ScaleIndication
import com.somnionocte.atomic_components.extensions.drawBackgroundBrush
import com.somnionocte.atomic_components.extensions.drawBackgroundColor
import com.somnionocte.atomic_components.extensions.drawBorderBrush
import com.somnionocte.atomic_components.templates.TemplateButton
import com.somnionocte.atomic_components.templates.TemplateCheckbox
import com.somnionocte.atomic_components.templates.TemplateRadioButton
import com.somnionocte.atomic_components.templates.TemplateSwitch
import com.somnionocte.atomic_components.ui.theme.FoundationComponentsTheme
import com.somnionocte.compose_extensions.animatableColorAs
import com.somnionocte.screen_router.NullScreen
import com.somnionocte.screen_router.Screen
import com.somnionocte.screen_router.ScreenRouter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FoundationComponentsTheme {
                CompositionLocalProvider(
                    LocalIndication provides ScaleIndication,
                    LocalContentColor provides MaterialTheme.colorScheme.onBackground
                ) {
                    NexusPortal {
                        ScreenRouter { screen ->
                            MainScreen()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        val scrollState = rememberScrollState()

        AtomicSurface(
            color = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ) {
            AtomicScaffold(
                top = {
                    val background1 = MaterialTheme.colorScheme.background
                    val background2 = MaterialTheme.colorScheme.surfaceContainer

                    val appBarColor by animatableColorAs {
                        if(scrollState.canScrollBackward) background2 else background1
                    }.asState()

                    AtomicAppBar(
                        designModifier = Modifier
                            .drawBackgroundColor { appBarColor }
                            .padding(16.dp)
                            .statusBarsPadding(),
                        textStyle = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.SemiBold),
                        start = {
                            TemplateButton(
                                onClick = {  },
                                modifier = Modifier.size(48.dp),
                                defaultColor = Color.Transparent,
                                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                                shape = RoundedCornerShape(50),
                                contentPadding = PaddingValues(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) { Icon(Icons.Rounded.AccountBox, null) }
                        },
                        center = { Text("Atomic Components") }
                    )
                }
            ) { innerPadding ->
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                        .imePadding()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Buttons()

                    Toggleables()

                    Inputs()
                }
            }
        }
    }

    @Composable
    fun Buttons() {
        Text("Buttons", fontSize = 32.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)

        FlowRow(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            itemVerticalAlignment = Alignment.CenterVertically
        ) {
            TemplateButton(
                onClick = {  },
                defaultColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shape = RoundedCornerShape(47),
                contentPadding = PaddingValues(24.dp, 16.dp),
            ) { Text("My Button") }

            TemplateButton(
                onClick = {  },
                defaultColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                shape = RoundedCornerShape(40),
                borderStroke = BorderStroke(5.dp, MaterialTheme.colorScheme.onTertiaryContainer),
                contentPadding = PaddingValues(24.dp, 16.dp),
            ) { Text("My Tertiary Button") }

            TemplateButton(
                onClick = {  },
                modifier = Modifier.size(48.dp),
                defaultColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                borderStroke = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(16.dp),
                contentPadding = PaddingValues(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) { Icon(Icons.Rounded.AccountBox, null) }

            AtomicButton(
                onClick = {  },
                contentColor = Color.Black,
                designModifier = Modifier
                    .drawBorderBrush(
                        shape = { RoundedCornerShape(40, 0, 40, 0) },
                        width = { 2.dp },
                        brush = { Brush.horizontalGradient(listOf(Color.Red, Color.Yellow)) }
                    )
                    .padding(2.dp)
                    .drawBackgroundBrush(
                        shape = { RoundedCornerShape(40, 0, 40, 0) },
                        brush = { Brush.horizontalGradient(listOf(Color.Green, Color.Cyan)) }
                    )
                    .padding(16.dp),
            ) { Text("My custom button") }

            TemplateButton(
                onClick = {  },
                modifier = Modifier.defaultMinSize(48.dp),
                defaultColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.primary,
                pressedColor = MaterialTheme.colorScheme.primary.copy(.3f),
                shape = RoundedCornerShape(45),
                contentPadding = PaddingValues(24.dp, 16.dp),
            ) { Icon(Icons.Rounded.PlayArrow, null) }

            TemplateButton(
                onClick = {  },
                modifier = Modifier.fillMaxWidth(),
                defaultColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(24.dp, 16.dp),
            ) { Text("My Loooong error Button") }
        }
    }

    @Composable
    fun Toggleables() {
        Text("Toggleables", fontSize = 32.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)

        Row(horizontalArrangement = Arrangement.Center) {
            var state1 by remember { mutableStateOf(false) }

            TemplateRadioButton(
                state1, { state1 = true },
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                backgroundCheckedColor = MaterialTheme.colorScheme.primaryContainer,
                checkColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )

            TemplateRadioButton(
                !state1, { state1 = false },
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                backgroundCheckedColor = MaterialTheme.colorScheme.primaryContainer,
                checkColor = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }

        Row(horizontalArrangement = Arrangement.Center) {
            var state2 by remember { mutableStateOf(false) }

            TemplateSwitch(
                state2, { state2 = it },
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                backgroundCheckedColor = MaterialTheme.colorScheme.primaryContainer,
                thumbColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                thumbCheckedColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Row(horizontalArrangement = Arrangement.Center) {
            var state3 by remember { mutableStateOf(false) }
            var state4 by remember { mutableStateOf(false) }

            TemplateCheckbox(
                if(state3 && state4) {
                    ToggleableState.On
                } else if(state3 || state4) {
                    ToggleableState.Indeterminate
                } else {
                    ToggleableState.Off
                }, {
                    state3 = true
                    state4 = true
                },
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                backgroundCheckedColor = MaterialTheme.colorScheme.primaryContainer,
                checkColor = MaterialTheme.colorScheme.onPrimaryContainer,
                borderStroke = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondaryContainer)
            )

            TemplateCheckbox(
                state3, { state3 = !state3 },
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                backgroundCheckedColor = MaterialTheme.colorScheme.primaryContainer,
                checkColor = MaterialTheme.colorScheme.onPrimaryContainer,
                borderStroke = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondaryContainer)
            )

            TemplateCheckbox(
                state4, { state4 = !state4 },
                backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                backgroundCheckedColor = MaterialTheme.colorScheme.primaryContainer,
                checkColor = MaterialTheme.colorScheme.onPrimaryContainer,
                borderStroke = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondaryContainer)
            )
        }
    }

    @Composable
    fun Inputs() {
        Text("Inputs", fontSize = 32.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)

        Column {
            AtomicTextField(
                rememberTextFieldState(),
                designModifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest, RoundedCornerShape(22.dp))
                    .padding(22.dp),
                textColor = MaterialTheme.colorScheme.onSurface,
                lineLimits = TextFieldLineLimits.SingleLine,
                placeholder = { Text("Type some text") },
                label = { Text("Label") },
                support = { Text("support") }
            )

            AtomicTextField(
                rememberTextFieldState(),
                designModifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest, RoundedCornerShape(22.dp))
                    .padding(22.dp),
                textColor = MaterialTheme.colorScheme.onSurface,
                prefix = { Text("$") },
                lineLimits = TextFieldLineLimits.SingleLine,
                placeholder = { Text("Type some text") },
                label = { Text("Label") },
                support = { Text("support") }
            )

            AtomicTextField(
                rememberTextFieldState(),
                designModifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest, RoundedCornerShape(22.dp))
                    .padding(22.dp),
                textColor = MaterialTheme.colorScheme.onSurface,
                suffix = { Text("$") },
                lineLimits = TextFieldLineLimits.SingleLine,
                placeholder = { Text("Type some text") },
                label = { Text("Label") },
                support = { Text("support") }
            )

            AtomicTextField(
                rememberTextFieldState(),
                designModifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest, RoundedCornerShape(22.dp))
                    .padding(22.dp),
                textColor = MaterialTheme.colorScheme.onSurface,
                prefix = { Text("+") },
                suffix = { Text("$") },
                lineLimits = TextFieldLineLimits.SingleLine,
                placeholder = { Text("Type some text") },
                label = { Text("Label") },
                support = { Text("support") }
            )

            AtomicTextField(
                rememberTextFieldState(),
                designModifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest, RoundedCornerShape(22.dp))
                    .padding(22.dp),
                textColor = MaterialTheme.colorScheme.onSurface,
                prefix = { Text("+") },
                suffix = { Text("$") },
                placeholder = { Text("Type some text") },
                label = { Text("Label") },
                support = { Text("support") }
            )
        }
    }

//    @Composable
//    fun RadioButton(
//        checked: Boolean,
//        onCheck: () -> Unit,
//        enabled: Boolean = true
//    ) {
//        TemplateRadioButton(
//            checked,
//            onCheck,
//            enabled = enabled,
//            backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
//            backgroundCheckedColor = MaterialTheme.colorScheme.primaryContainer,
//            checkColor = MaterialTheme.colorScheme.onPrimaryContainer,
//        )
//    }
//
//    @Composable
//    fun Switch(
//        checked: Boolean,
//        onCheck: (Boolean) -> Unit,
//        enabled: Boolean = true
//    ) {
//        TemplateSwitch(
//            checked,
//            onCheck,
//            enabled = enabled,
//            backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
//            backgroundCheckedColor = MaterialTheme.colorScheme.primaryContainer,
//            thumbColor = MaterialTheme.colorScheme.surfaceContainerLowest,
//            thumbCheckedColor = MaterialTheme.colorScheme.onPrimaryContainer
//        )
//    }
//
//    @Composable
//    fun CheckBox(
//        checked: Boolean,
//        onCheck: (Boolean) -> Unit,
//        enabled: Boolean = true
//    ) {
//        TemplateCheckbox(
//            checked,
//            onCheck,
//            enabled = enabled,
//            backgroundColor = MaterialTheme.colorScheme.surfaceContainerHigh,
//            backgroundCheckedColor = MaterialTheme.colorScheme.primaryContainer,
//            checkColor = MaterialTheme.colorScheme.onPrimaryContainer,
//            borderStroke = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondaryContainer)
//        )
//    }
//
//    @Composable
//    fun MyAppBar(
//        title: String,
//        onBackClick: () -> Unit = {  }
//    ) {
//        TemplateLargeAppBar(
//            navigation = {
//                IconButton(onBackClick) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, null) }
//            },
//            header = {
//                Text(title, Modifier.padding(start = 8.dp), fontWeight = FontWeight.SemiBold)
//            },
//            largeHeader = {
//                Text(
//                    title, Modifier.padding(start = 8.dp, bottom = 8.dp),
//                    fontSize = 28.sp, fontWeight = FontWeight.SemiBold
//                )
//            },
//            inactiveBackgroundColor = MaterialTheme.colorScheme.background,
//            backgroundColor = MaterialTheme.colorScheme.surfaceContainerLowest,
//            contentPadding = PaddingValues(16.dp, 8.dp, 16.dp, 8.dp)
//        )
//    }
//
//    @Composable
//    fun MyBottomBar(content: @Composable RowScope.() -> Unit) {
//        TemplateCollapseBottomBar(
//            modifier = Modifier.fillMaxWidth(),
//            backgroundColor = MaterialTheme.colorScheme.surfaceContainerLowest,
//            content = content
//        )
//    }
//
//    @Composable
//    fun Button(index: Int, enabled: Boolean = true, onClick: () -> Unit = {  }) {
//        TemplateButton(
//            onClick = onClick,
//            enabled = enabled,
//            shape = RoundedCornerShape(45),
//            contentPadding = PaddingValues(24.dp),
//            backgroundColor = MaterialTheme.colorScheme.primaryContainer,
//            pressedBackgroundColor = MaterialTheme.colorScheme.run { primaryContainer.mix(.25f, onPrimaryContainer) },
//            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
//        ) {
//            Text("Foundation Button $index")
//        }
//    }
//
    val textStyleTextField = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium)

    @Composable
    fun TextField(
        value: String,
        onChange: (value: String) -> Unit,
        modifier: Modifier = Modifier,
        enabled: Boolean = true,
        readOnly: Boolean = false,
        textStyle: TextStyle = textStyleTextField,
        label: @Composable() (() -> Unit)? = null,
        placeholder: @Composable() (() -> Unit)? = null,
        prefix: @Composable() (() -> Unit)? = null,
        suffix: @Composable() (() -> Unit)? = null,
        supportingText: @Composable() (() -> Unit)? = null,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,
        singleLine: Boolean = false,
        maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
        minLines: Int = 1,
        backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
        shape: RoundedCornerShape = RoundedCornerShape(28.dp),
        padding: PaddingValues = PaddingValues(22.dp),
        interactionSource: MutableInteractionSource? = null
    ) {
        AtomicTextField(
            value = value,
            onChange = onChange,
            modifier = modifier
                .background(backgroundColor, shape)
                .padding(padding),
            enabled = enabled,
            readOnly = readOnly,
            textStyle = LocalTextStyle.current.merge(textStyle),
            label = label,
            placeholder = placeholder,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            supportingTextArrangement = Arrangement.SpaceBetween,
            supportingTextModifier = Modifier,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            interactionSource = interactionSource,
            innerTextFieldGap = 16.dp,
            cursorColor = LocalContentColor.current,
        )
    }
}