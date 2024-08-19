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
