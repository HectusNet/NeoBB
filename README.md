# NeoBB

## How to Compile & Use

This guide will get you to running the plugin on your own. You'll need to manually compile and then setup your own server with the compiled plugin.

### Downloading the Code

To compile the plugin, you are gonna need to clone the repository onto your computer. To do that, I recommend just using the GitHub Desktop app or the IDE of your choice, as they provide a very simple and easy to use interface, without any manual commands required.  
Additionally, you can use this command to clone it using the command line:

```bash
# Clone the Repository (requires Git to be installed):
git clone https://github.com/HectusNet/NeoBB

# Change Directory to the Downloaded Files:
cd NeoBB
```

### Compiling

To compile the plugin, you will need to use Gradle. The easiest way is just running one of these commands:

```bash
# Using Gradle Installation:
gradle build

# Built-in UNIX Bash Script:
./gradlew build

# Built-in Windows Batch Script:
gradlew.bat build
```

After this, you will have a JAR file at `./build/libs/NeoBB-*.jar`

> [!WARNING]
> This plugin is not yet meant for production usage, so using it in a production server has a slim chance of adding critical bugs or issues.
> The developers are not responsible for any damage caused on your server.

### Running

To run the plugin, you will need to create a PaperMC or PurPur server (just watch a tutorial).  
After that, you can put the official world or a custom one into the server files and add the built `NeoBB-*.jar` file into the `plugins/` folder.
Afterwards, just run the server like you usually would and start a match using `/games start <mode> <players>`.

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

> The method parameters are just the Locale of the player, using `Player#locale()` or `NeoPlayer#locale()`, followed by the translation key and then additionally values for the placeholders. Placeholders should look be `{...}` with a parameter index instead of `%s`, to allow for translations into languages that change sentence structures.

For even more information, please read the [Translation JavaDocs](https://marcpg.com/jd/LibPG/com/marcpg/libpg/lang/Translation.html).

#### Colors

For the colors, please try to use the ones provided in the utility class `Colors`, instead of using `NamedTextColor` or `TextColor` directly. It provides you with an easy color scheme to make the plugin not look chaotic. It provides you with accent colors, secondary colors, and more.

### Adding new Turns

Adding new turns is really simple. All you need to do is create a class which meets these requirements:
1. Starts with the game mode's letter (like L for Legacy) and a T for Turn, like `TTurnName`, `TPurpleWool`, or `LTLegacyTurn`.
2. Extends the `Turn<?>` interface, where the type parameter should be the class of the turn's data. For some games, you can also use usage-specific turn classes, like `BlockTurn` and `ItemTurn`, which will also automate some stuff like the getting the required item for you.
3. Implement the necessary interfaces based on the game it's for.
4. Implement all required methods, like the `cost()`, `item()`, etc. and additionally also other methods like `apply()`.
5. Add the turn into the game's `GameInfo` object. It is at the beginning of every game class and shouldn't be hard to find.
6. Add it to the events in `TurnEvents` or create a new event method in it.

> [!IMPORTANT]
> Please just ask MarcPG for help if you are working on it for the first time, because it is a lot easier to explain in person.
