# NeoBB

## How to Compile & Use

This guide will get you to running the plugin on your own. You'll need to manually compile and then setup your own server with the compiled plugin.

### Downloading the Code

To compile the plugin, you are gonna need to clone the repository onto your computer. To do that, I recommend just using the GitHub Desktop app, as it provides a very simple and easy to use interface, without any command line.  
Additionally, you can use this command to clone it using the command line:

```bash
# Clone the Repository (requires Git to be installed):
git clone https://github.com/HectusNet/NeoBlockBattles

# Change Directory to the Downloaded Files:
cd NeoBlockBattles
```

### Compiling

To compile the plugin, you will need to use gradle. The easiest way is just running this command:

```bash
# Using Gradle Installation:
gradle build

# Built-in Batch Script:
./gradlew build

# Built-in Windows Script:
gradlew.bat build
```

After this, you will have a JAR file at `./build/libs/NeoBB-*.jar`

### Running

To run the plugin, just create a PaperMC or PurPur server (just use Google).  
After that, you can put the official world or a custom one into the server files and add the built `NeoBB-*.jar` file into the `plugins/` folder.
Afterwards, just run the server like you usually would and start a match using `/start <players...>`.

## Development

When contributing to this project, please keep these things in mind!

### Text Format

#### Components

When using any form of text, like sending messages to players, showing titles, scoreboards, or really anything else, please always try to use Components provided by the adventure-api.  
PaperMC has built-in support for them and even if they might seem more complicated than just Strings, their possibilities are basically endless and it allows for some insane text formatting, like colors, formatting, hover text, click events, etc.  

#### Translations

Preferably, you can also use translations instead of just text. To add a new translation and use it, you can just add it to the `en_US.properties` file.  
For the translation to actually be usable, please contact MarcPG and ask him to upload the newest translations to the server.  
To actually use translations, you can use the `Translation` class provided by LibPG. There are two translation types:

- `Translation#string(...)` will return the translation as a string. Not recommended.
- `Translation#component(...)` will return it as a component, which is preferred and allows for more formatting.

> The method parameters are just the Locale of the player, using `Player#locale()` or `NeoPlayer#locale()`, followed by the translation key and then additionally values for the placeholders.  

For more information, like parameters, please read the JavaDocs of the `Translation` class.

#### Colors

For the colors, please try to use the colors provided in the utility class `Colors`, instead of using `NamedTextColor` or `TextColor` directly. It provides you with the basic colors as they should be used, with accent colors and more.

### Adding new Turns

Adding new turns is really simple. All you need to do is create a class which meets these requirements:
1. Starts with a T, like `TTurnName` or `TPurpleWool`.
2. Extends the `Turn<?>` interface, where the type parameter should be the class of the turn's data. Check next point for more info.
3. Implement the usage class of the turn, like `BlockUsage` or `MobUsage`. You can check the interface's code to see what class to use as a type parameter for the second point.
4. Implement one or more function classes, like `AttackFunction` or `CounterbuffFunction`. You should always try using only one function interface, if possible.
5. Implement the turn's item class, like `NeutralClazz` or `HotClazz`.
6. Implement all necessary methods, like the `cost()`, `item()`, etc. and additionally also other methods like `apply()`
7. The `getValue()` method should always just return the `data` object.
8. Add the turn into the game's `GameInfo` object. It is at the beginning of every game class and shouldn't be hard to find.
9. Add it to the events in `TurnEvents` or create a new event method in `TurnEvents`.

> You can simplify this process by just copy-pasting the `TExample` class and modifying your values. It has the constructors very compressed for you and everything else, like the `getValue()` method already implemented.  
> Don't forget to change the `item()` and `cost()` methods please, as they are very important!
