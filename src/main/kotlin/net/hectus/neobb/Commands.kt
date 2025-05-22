package net.hectus.neobb

import com.marcpg.libpg.storing.Cord
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import net.hectus.neobb.external.cosmetic.PlaceParticle
import net.hectus.neobb.external.cosmetic.PlayerAnimation
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.game.mode.CardGame
import net.hectus.neobb.game.mode.DefaultGame
import net.hectus.neobb.game.mode.PersonGame
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.matrix.structure.Structure
import net.hectus.neobb.matrix.structure.StructureManager
import net.hectus.neobb.util.Colors
import net.hectus.util.asCord
import net.hectus.util.component
import net.hectus.util.enumValueNoCase
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.World
import org.bukkit.entity.Player
import java.util.*

@Suppress("UnstableApiUsage")
object Commands {
    val MODES = listOf("default", "card", "person98")

    fun games() : LiteralCommandNode<CommandSourceStack> = Commands.literal("game")
        .requires { source -> source.sender.hasPermission("neobb.games") }
        .then(Commands.literal("start")
            .then(Commands.argument("mode", StringArgumentType.word())
                .suggests { _, builder ->
                    MODES.forEach { builder.suggest(it) }
                    return@suggests builder.buildFuture()
                }
                .then(Commands.argument("difficulty", StringArgumentType.word())
                    .suggests { _, builder ->
                        GameDifficulty.entries.forEach { builder.suggest(it.name.lowercase()) }
                        return@suggests builder.buildFuture()
                    }
                    .then(Commands.argument("players", ArgumentTypes.players())
                        .executes { context ->
                            val source = context.source.sender
                            val locale = if (source is Player) source.locale() else Locale.getDefault()

                            val difficulty: GameDifficulty = enumValueNoCase(context.getArgument("difficulty", String::class.java))
                            val players = context.getArgument("players", PlayerSelectorArgumentResolver::class.java).resolve(context.source)

                            if (players.size < 2) {
                                source.sendMessage(locale.component("command.games.start.not_enough_players", color = Colors.NEGATIVE))
                                return@executes 1
                            }

                            source.sendMessage(locale.component("command.games.start.starting", color = Colors.POSITIVE))
                            runCatching {
                                when (context.getArgument("mode", String::class.java)) {
                                    "default" -> DefaultGame(players.last().world, players, difficulty).init()
                                    "card" -> CardGame(players.last().world, players, difficulty).init()
                                    "person98" -> PersonGame(players.last().world, players, difficulty).init()
                                    else -> source.sendMessage(locale.component("command.games.start.unknown_mode", color = Colors.NEGATIVE))
                                }
                            }.onFailure { e ->
                                source.sendMessage(locale.component("command.games.start.error", color = Colors.NEGATIVE))
                                NeoBB.LOG.error("Could not start match!", e)
                            }
                            return@executes 1
                        }
                    )
                )
            )
        )
        .then(Commands.literal("stop")
            .then(Commands.argument("id", StringArgumentType.word())
                .suggests { _, builder ->
                    GameManager.GAMES.keys.forEach { builder.suggest(it) }
                    return@suggests builder.buildFuture()
                }
                .executes { context ->
                    val source = context.source.sender
                    val locale = if (source is Player) source.locale() else Locale.getDefault()

                    val game = GameManager[context.getArgument("id", String::class.java)]

                    if (game == null) {
                        source.sendMessage(locale.component("command.games.start.not_enough_players", color = Colors.NEGATIVE))
                        return@executes 1
                    }

                    game.end(true)
                    source.sendMessage(locale.component("command.games.stop.success", game.id, color = Colors.NEUTRAL))

                    return@executes 1
                }
            )
        )
        .then(Commands.literal("list")
            .executes { context ->
                val source = context.source.sender
                if (GameManager.GAMES.isEmpty()) {
                    source.sendMessage(Component.text("There are no games running.", Colors.NEUTRAL))
                } else {
                    source.sendMessage(Component.text("Running Games:"))
                    for (game in GameManager.GAMES.values) {
                        source.sendMessage(Component.text("==== ", Colors.EXTRA).append(Component.text(game.id, Colors.ACCENT)).append(Component.text(" ====", Colors.EXTRA)))
                        source.sendMessage(Component.text("> Players: ", Colors.EXTRA).append(Component.text("${game.players.size}/${game.initialPlayers.size}", Colors.SECONDARY)))
                        source.sendMessage(Component.text("> Time: ", Colors.EXTRA).append(Component.text(game.timeLeft.preciselyFormatted, Colors.SECONDARY)))
                        source.sendMessage(Component.text("> Difficulty: ", Colors.EXTRA).append(Component.text(game.difficulty.name, Colors.SECONDARY)))
                        source.sendMessage(Component.text("> Played Turns: ", Colors.EXTRA).append(Component.text(game.history.size, Colors.SECONDARY)))
                        source.sendMessage(Component.text("> Turning: ", Colors.EXTRA).append(Component.text(game.currentPlayer().name(), Colors.SECONDARY)))
                    }
                }
                return@executes 1
            }
        )
        .build()

    fun giveup() : LiteralCommandNode<CommandSourceStack> = Commands.literal("giveup")
        .requires { source -> source.sender is Player }
        .executes { context ->
            val sender = context.source.sender
            if (sender !is Player) return@executes 1

            val player = GameManager.player(sender)

            if (player != null && player.game.started) {
                sender.sendMessage(sender.locale().component("command.giveup.confirm", color = Colors.NEUTRAL))
                player.game.eliminate(player)
                return@executes 1
            }
            sender.sendMessage(sender.locale().component("command.not_in_game", color = Colors.NEGATIVE))
            return@executes 1
        }
        .build()

    fun structure() : LiteralCommandNode<CommandSourceStack> = Commands.literal("structure")
        .requires { source -> source.sender.hasPermission("neobb.structures") }
        .then(Commands.literal("save")
            .then(Commands.argument("name", StringArgumentType.word())
                .then(Commands.argument("world", ArgumentTypes.world())
                    .then(Commands.argument("corner1", ArgumentTypes.blockPosition())
                        .then(Commands.argument("corner2", ArgumentTypes.blockPosition())
                            .executes { context ->
                                val source = context.source.sender
                                val name = context.getArgument("name", String::class.java)

                                if (StructureManager[name] != null) {
                                    source.sendMessage(Component.text("Structure with name \"$name\" already exists!", Colors.NEUTRAL))
                                    source.sendMessage(Component.text("You can remove it using \"/structure remove $name\"!", Colors.EXTRA))
                                    return@executes 1
                                }

                                val world = context.getArgument("world", World::class.java)
                                val corner1 = context.getArgument("corner1", BlockPositionResolver::class.java).resolve(context.source).toLocation(world)
                                val corner2 = context.getArgument("corner2", BlockPositionResolver::class.java).resolve(context.source).toLocation(world)

                                val structure = Structure(name, world, corner1.asCord(), corner2.asCord())
                                structure.save()
                                StructureManager.add(structure)

                                source.sendMessage(Component.text("Successfully saved structure with name \"${structure.name}\".", Colors.POSITIVE))
                                return@executes 1
                            }
                        )
                    )
                )
            )
        )
        .then(Commands.literal("remove")
            .then(Commands.argument("name", StringArgumentType.word())
                .suggests { _, builder ->
                    StructureManager.LOADED.forEach { builder.suggest(it.name) }
                    return@suggests builder.buildFuture()
                }
                .executes { context ->
                    val source = context.source.sender
                    val name = context.getArgument("name", String::class.java)

                    val structure = StructureManager[name]
                    if (structure != null) {
                        StructureManager.remove(structure)
                        context.source.sender.sendMessage(Component.text("Successfully removed structure with name: $name", Colors.NEUTRAL))
                    } else {
                        context.source.sender.sendMessage(Component.text("Could not find structure with name: $name", Colors.NEGATIVE))
                        return@executes 1
                    }

                    source.sendMessage(Component.text("Successfully removed structure with name \"${structure.name}\".", Colors.NEUTRAL))
                    return@executes 1
                }
            )
        )
        .then(Commands.literal("list")
            .executes { context ->
                val source = context.source.sender
                if (StructureManager.LOADED.isEmpty()) {
                    source.sendMessage(Component.text("There are no structures loaded.", Colors.NEUTRAL))
                } else {
                    source.sendMessage(Component.text("Loaded Structures:"))
                    for (structure in StructureManager.LOADED) {
                        source.sendMessage(Component.text("- " + structure.name + "(" + structure.materials.size + ")"))
                    }
                }
                return@executes 1
            }
        )
        .then(Commands.literal("place")
            .then(Commands.argument("name", StringArgumentType.word())
                .suggests { _, builder ->
                    StructureManager.LOADED.forEach { builder.suggest(it.name) }
                    return@suggests builder.buildFuture()
                }
                .then(Commands.argument("location", ArgumentTypes.blockPosition())
                    .executes { context ->
                        val source = context.source.sender
                        val name = context.getArgument("name", String::class.java)
                        val location = context.getArgument("location", BlockPositionResolver::class.java).resolve(context.source).toLocation(context.source.location.world)

                        val structure = StructureManager[name]
                        if (structure != null) {
                            structure.place(location, false)
                            context.source.sender.sendMessage(Component.text("Successfully placed structure with name: $name", Colors.POSITIVE))
                        } else {
                            context.source.sender.sendMessage(Component.text("Could not find structure with name: $name", Colors.NEGATIVE))
                            return@executes 1
                        }

                        source.sendMessage(Component.text("Successfully placed structure with name \"${structure.name}\".", Colors.NEUTRAL))
                        return@executes 1
                    }
                )
            )
        )
        .build()

    fun debug() : LiteralCommandNode<CommandSourceStack> = Commands.literal("debug")
        .requires { source -> source.sender.hasPermission("neobb.debug") }
        .then(Commands.literal("effect")
            .then(Commands.literal("player-animation")
                .then(Commands.argument("player", ArgumentTypes.player())
                    .then(Commands.argument("animation", StringArgumentType.word())
                        .suggests { _, builder ->
                            PlayerAnimation.entries.forEach { builder.suggest(it.name.lowercase()) }
                            return@suggests builder.buildFuture()
                        }
                        .executes { context ->
                            val animation: PlayerAnimation = enumValueNoCase(context.getArgument("animation", String::class.java))
                            val target = context.getArgument("player", PlayerSelectorArgumentResolver::class.java).resolve(context.source).first()
                            animation.play(target, target.location.asCord().subtract(Cord(4.0, 0.0, 4.0)))

                            context.source.sender.sendMessage(Component.text("Successfully played player-animation with name \"${animation.name}\".", Colors.POSITIVE))
                            return@executes 1
                        }
                    )
                )
            )
            .then(Commands.literal("place-particle")
                .then(Commands.argument("block", ArgumentTypes.blockPosition())
                    .then(Commands.argument("particle", StringArgumentType.word())
                        .suggests { _, builder ->
                            PlaceParticle.entries.forEach { builder.suggest(it.name.lowercase()) }
                            return@suggests builder.buildFuture()
                        }
                        .executes { context ->
                            val world = context.source.executor?.world ?: Bukkit.getWorld("world")!!
                            val block = context.getArgument("block", BlockPositionResolver::class.java).resolve(context.source).toLocation(world)

                            val particle: PlaceParticle = enumValueNoCase(context.getArgument("particle", String::class.java))
                            particle.spawn(block)

                            context.source.sender.sendMessage(Component.text("Successfully shown place-particle with name \"${particle.name}\".", Colors.POSITIVE))
                            return@executes 1
                        }
                    )
                )
            )
        )
        .build()
}
