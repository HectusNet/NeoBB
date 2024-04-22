# Hectus NeoBB (NeoBlockBattles)

The new and better version of the old Hectus BlockBattles.

## How to Run

### Run Server

There are two different ways of running the plugin on a server, which are:

---

Run manually by building:
> To run the plugin manually, just build the JAR file using `gradle build` and put it into your server's plugin directory.
> Things like the English translations and the rest of the necessary files will be created automatically.

---

Run directly with debugging support (recommended):
> To run the plugin directly using the `run-paper` Gradle plugin, you can just use `gradle runServer`, which can also be run in debug mode, which allows you to use breakpoints and more for a better view of what happened.
> All files will be automatically created in a directory called `run` and you will only need to agree to the EULA. The rest is handled automatically.

---

### Start a Match

To start a match, you can use the command `/challenge` followed by another player's name. The other player will then receive a challenge message, which you can accept by clicking the button.  
After that, the shop will pop up and you'll be able to buy your items and then continue with the main game.

## Contributing

### Code

Here are some things you should keep in mind when contributing:
1. When creating complicated methods that may not be easy to use, please write a little JavaDoc. You can do that by typing `/**`, then hitting enter and filling out the stuff.
2. Please follow basic Java code conventions, as found [here](https://www.oracle.com/technetwork/java/codeconventions-150003.pdf).

### Committing

When committing, please always try to not commit too much and not too little.  
The optimal amount is for each feature or major bug fix. Commits that only contain a small bug fix or a half-finished feature aren't worth clogging up the commit history. Too big is stuff like full updates or multiple major features.
