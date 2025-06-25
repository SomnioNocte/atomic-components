# Atomic Components

A library whose purpose is to replace Material library for designing your own design system. Minimalistic, without bias to a particular design and with a minimum of dependencies.

### Why?

### How to use?

The library is divided into two parts: Atomic and Template. 

Atomic components are design-agnostic, meaning they come without pre-defined styling. The only way to style them is through modifiers, which offers great flexibility and convenience. Think of them as pure HTML elements without CSS, but with `appearance: none;` applied.

Template в свою очередь это компоненты основанны на Atomic принимающие простые стилистические аргументы такие как background, shape, border и т.д. Я сделал их для простых сценариев и они способны удволетворить большинство простые системы дизайны, в которых нет особых форм, градиентов и стилистических анимаций.

Хоть Template компоненты звучат удобно, я рекомендую идти с более низкоуровневыми компонентами (Atomic) и привыкать использовать их со всеми сопутсвующими оптимизациями испольузя лямбды модификаторы напрямую, а не передавая аргументы для разных состояний (к примеру нажатий кнопки) и тем самым вызывая рекомпозиции, которых в таком сценарии не избежать.