# Foundation Components

A library whose purpose is to replace Material library for designing your own design system. 
Minimalistic, without bias to a particular design and with a minimum of dependencies.

### Why?

I build my own apps with my own design decisions. From slight modifications of standard material you components, it turned into that I wanted to create my own design system. My design system still takes the best from material you and Apple's HIG, but still these small changes have become so many that it has the right to be called its own design system.

There is no reason to create a design system that follows its own rules and has its components based on another whose ideas and look are different from the first. It even sounds silly. Bad for the developer, because you have to "argue" and "break" the standard library and end up with the end user device doing the work of the design system, and then doing the work of rewriting to another design system. It is clear that in practice it is unlikely that modern phones will have problems with this, but this does not change the fact that everyone does extra work, which would not have been initially if in the foundation compose library along with `box`, `column` and `basicTextField`, there were other such basic button, basic switch and so on. It may seem that it is not a big problem and material library has enough customisation, but then sooner or later you write components based on `box` with its own state.

**In short**, compose lacks basic components without native styling, like how HTML has raw elements without any CSS.

The idea to create a library of basic components without material you dependencies did not come at once. I was thinking about where to start writing my design system and I came across [this article](https://proandroiddev.com/opinion-jetpack-compose-needs-a-design-system-layer-dc579fde79b2?gi=49210bf6174d), after that I thought: I'm going to do the same work anyway, why don't I just make it modular and then put it out as a separate library.

The library doesn't have to be super-customizable and overloaded, hello 80/20 rule. For most use cases, these simple customizations will suffice.

Again, the main problem is not that the material library does not represent enough configuration (it shouldn't, as an independent design system), but that initially worth avoid unnecessary layer of code and work, and minimize dependencies. Also there is no criticism of the material, a wonderful design system that I am inspired by for mine.

**So after all this ranting, in a nutshell why?**

Components without style. Components without the bias of the design system. Just an empty component like HTML nodes without CSS.

###  How to use

Most components have two versions: raw and animated. In most cases, start with the animated one. The animated ones are based on the raw ones, and they differ only in their styling approach. Animated ones take additional arguments such as background color, shape and border, while raw ones don't have any visual arguments at all. A raw component can only be styled through a modifier, which allows you to push the boundaries in styling to a very large scale. Under the hood, animated ones use an InteractionSource and apply customizable out-of-the-box solutions in the animation (for example) of a push, adding arguments to background colors, border, shape and animationSpec. While this sounds like a library that assumes the use of low-level components without its own stylized view, it is (to me) reasonable here, since all this convenience that covers most use cases, most simple design systems will suffice with the customizations that animated components offer, delegating animations to themselves.

At the moment there are:

* `FoundationButton` / `AnimatedFoundationButton`
* `FoundationTextField`
* `coreScrollbar`
* `Text`

A couple of simple examples:

**FilledButton from MaterialYou**
```kotlin
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
```

**Nice and simple text field with colored border**
```kotlin
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
```

**Result:**

https://github.com/user-attachments/assets/03a95138-85f5-48b8-95be-096081478a1b