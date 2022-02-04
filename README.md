# PLUS Board Game Framework

A custom made board game framework including a (graphical) user interface framework and a networking framework, as well as the implementation of the games Ludo (De.: "Mensch ärgere Dich nicht!") and Mill ("Mühle").

---

## About

This repository and project was made as part of the Software Practicum course of the Paris Lodron University of Salzburg in the winter semester 2021/22. The goal was to create a board game framework which allows you to easily implement multiplayer board games and also to create a board game with it.

Next is a list of projects implemented using this framework. For more images and screenshots of the games you can also check out our [January 20th, 2022 presentation pdf](https://github.com/CAS-ual-TY/plus-board-game-framework/blob/main/presentation_jan_20_2022.pdf).

**Board Game Framework**

For the framework we implemented two independent sub-frameworks - the (graphical) user interface framework and the networking framework. Built on top of these we built the actual game engine and board game framework. It is worth mentioning again that the sub-frameworks can be used independently.

**Ludo**

The first board game we implemented was Ludo (De.: "Mensch ärgere Dich nicht!"). You are able to host games or connect to lobbies. The game is fully animated - moving the figures or rolling the dice plays an animation. The game can also be played with 2-6 players, with the board being adaptive to the amount of players who join (the algorithm theoretically allows to create a board for an infinite amount of players, but the screen size is not big enough to support that let alone other components like networking etc.). The game is built on textures entirely and supports both English and German.

![ludo](https://user-images.githubusercontent.com/41904979/152607421-5371831d-1b42-41a0-a6c3-9ca67ee5b971.png)

**Mill**

We implemented Mill (De.: "Mühle") in a single day of actual work time to proof how easily you can make a board game using our framework. This is the second project we implemented using it.

![mill](https://user-images.githubusercontent.com/41904979/152607434-3801c0e3-79ef-48ee-a3ad-404edaa4503a.png)

**Online Chat Demo**

To test the advanced networking framework a simple online chat "game" has been implemented with custom screens or chat notifications for different networking events, like when someone connects, disconnectes or reconnects, when the server is closed, then the connection has been lost, ... The demo is kept relatively basic compared to the games we have implemented and can be used as an example on how to effectively use this framework.

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

The `Dimensions` class represents the position and size of every widget. Widgets are adaptive to the screen size both in position and size. They are based on an `AnchorPoint` and offset from that point. These anchor points are always relative to the screen size. The anchor points are as follows:

![anchor_points](https://user-images.githubusercontent.com/41904979/152374529-2b61b445-87bd-4bc0-bb0f-062d4cbd3870.png)

There is also a second, inner anchor point, however, for each widget. This inner anchor point defines the zero point coordinate of every widget.

The following image shows different widgets with different widths and heights (either 100 or 200), different horizontal and vertical offsets (+/- 200), different anchor points (eg. the yellow, green and blue widgets use the anchor point `M` which stands for "middle", red widgets use the anchor points `M`, `R` and `BR` in clockwise order), different inner anchor points (`BR` for the red widgets, `M` for the yellow one, `BL` for the green one and `T` for the blue one); the black line represents the offset, the point where the black line is connected with the widget represents the inner anchor points and the other side of the black line represents the main anchor point:

![widgets](https://user-images.githubusercontent.com/41904979/152374578-31287968-5e59-457d-96aa-68587c044d0a.png)

**Styles**

The `Style` class defines the visual representation of widgets. Styles can be eg. simple colored or textured quads or borders, constant or dynamic text, nothing at all... Styles are fully combineable, stackable and can also be functionally combined, like arranging them vertically or making them dependent on if the mouse is being hovered over them (decorator design pattern).

### Networking

**Introduction**

*There are actually two different networking systems: The basic system and the advanced one. We only consider the advanced systems with all advanced classes and interfaces.*

Networking is designed to be very easily usable. It uses a Host-Client structure (a host is a server that is also a client) and the entire multithreaded functionality is hidden behind simple interfaces which only interact with the main thread.

It is a messaged based networking protocol built on TCP. One interface instance is returned and used for active interaction (like sending a message) and one interface has to be implemented and is used for passive interaction (event handling, like being notified when a client connected). The protocol is created by registering messages - for each message there must be an encoder (turn the message into bytes), a decoder (turn the bytes back into the message) and a handler (what to do with the message, ran on the main thread).

Some of the features of this networking sub-framework are as follows:
- Messages are always run in the same order they were sent in
- Simple authentication and player names included
- Orderly and unorderly (connection timeout) disconnects are detected
- Basic protocol included, like orderly kicking clients with a message
- Use your own class as client representation and for network-based interaction on server-side

**Usage**

Usage is very easy. Call either `NetworkHelper#advancedConnect` to connect to a server, or `NetworkHelper#advancedHost` to host a server. As a return value, you either get an `IAdvancedClientManager` instance, or an `IAdvancedHostManager` instance. The parameters are very self explanatory. The passed `IAdvancedClientEventsListener` instance is used to react to certain client events, `IAdvancedHostEventsListener` for server events. Since the host is also a client, all host interfaces also extend all client interfaces, with some methods having gotten a default implementation for various reasons (eg. the client-side connection-lost event is of no use for the host, as they can not lose connection to the server they host themselves). It is the responsibility of the caller to keep the manager instances and to update them (eg. call `IAdvancedClientManager#update` or `IAdvancedHostManager#update` from your main game class's `update` method).

The `IAdvancedMessageRegistry` parameter basically defines your used message protocol (use the `AdvancedMessageRegistry` class). This parameter should always be the same for both client and host. To differentiate between which side the code is currently being ran on (client or server), the handler receives an `Optional<C>` which is empty on client-side and contains the object representing the client on server-side.

`C` is your own player class that gets instantiated for each client. This generic is used for almost all networking classes and interfaces. All that needs to be done is to implement `IAdvancedClient` in `C`.

**Demo**

The repository contains a networking demo which is a simple online chat including a proper graphical user interface. It is kept simple and uses almost all networking functionality.
