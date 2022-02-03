# PLUS Board Game Framework

A custom made board game framework including a (graphical) user interface framework and a networking framework, as well as the implementation of the games Ludo (De.: "Mensch ärgere Dich nicht!") and Mill ("Mühle").

---

## About

Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.

---

## Setup

Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.

---

## Usage

### Getting Started

Your game class must implement the `IGame` interface. Then call `Engine#runGame` with your game instance as parameter. Your game instance should be a singleton. Implement the `IGame#init` method to do any initialization that requires engine resources. `IGame` extends `IScreenHolder` and as such your game instance is always required to return a non-null `Screen` instance via the implemented method `IScreenHolder#getScreen` (use `init` for instantiation of the first `Screen`). The `getScreen` method defines which `Screen` is being rendered. Changing the returned value to another `Screen` will automatically initialize said new instance (implement `IScreenHolder#setScreen` for that as a plain setter).

### Screens

A `Screen` basically defines the graphical user interface presented to the player. It is made up of a list of `Widget` instances which are single elements with single responsibilities in functionality and visuals. Create a new class extending `Screen` and add widgets in the constructor by calling `widgets.add(...)`. The parameters of `Widget` classes are as follows:

**Dimensions**

The `Dimensions` class represents the position and size of every widget. Widgets are adaptive to the screen size. They are based on an `AnchorPoint` and offset from that point. These anchor points are always relative to the screen size. The anchor points are as follows:

![anchor_points](https://user-images.githubusercontent.com/41904979/152374529-2b61b445-87bd-4bc0-bb0f-062d4cbd3870.png)

There is also a second, inner anchor point, however, for each widget. This inner anchor point defines the zero point coordinate of every widget.

The following image shows different widgets with different widths and heights (either 100 or 200), different horizontal and vertical offsets (+/- 200), different anchor points (eg. the yellow, green and blue widgets use the anchor point `M` which stands for "middle", red widgets use the anchor points `M`, `R` and `BR` in clockwise order), different inner anchor points (`BR` for the red widgets, `M` for the yellow one, `BL` for the green one and `T` for the blue one); the black line represents the offset, the point where the black line is connected with the widget represents the inner anchor points and the other side of the black line represents the main anchor point:

![widgets](https://user-images.githubusercontent.com/41904979/152374578-31287968-5e59-457d-96aa-68587c044d0a.png)

**Styles**

The `Style` class defines the visual representation of widgets. Styles can be eg. simple colored or textured quads or borders, constant or dynamic text, nothing at all... Styles are fully combineable, stackable and can also be functionally combined, like arranging them vertically or making them dependent on if the mouse is being hovered over them (decorator design pattern).

### Networking

Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
