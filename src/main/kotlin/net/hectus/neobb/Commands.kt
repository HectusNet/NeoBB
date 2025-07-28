@file:Suppress("UnstableApiUsage")

package net.hectus.neobb

import com.marcpg.libpg.sarge.*
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.*
import com.mojang.brigadier.arguments.StringArgumentType
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import net.hectus.neobb.external.cosmetic.PlaceParticle
import net.hectus.neobb.external.cosmetic.PlayerAnimation
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.matrix.structure.Structure
import net.hectus.neobb.matrix.structure.StructureManager
import net.hectus.neobb.util.Colors
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit

object Commands {
    val game = command("game") {
        require("neobb.games")
        subcommand("start") {
            argument("mode", ExtendedArgumentTypes.valued { Registry.modes.keys }.paper()) {
                argument("difficulty", ExtendedArgumentTypes.enum(GameDifficulty::class.java).paper()) {
                    argument("players", ArgumentTypes.players()) {
                        action { context ->
                            val source = context.exec()

                            val difficulty = context.arg("difficulty", GameDifficulty::class.java)
                            val players = context.arg("players", ArgumentTypes.players()).resolve(context.source)

                            if (players.size < 2)
                                return@action source.locale().component("command.games.start.not_enough_players", color = Colors.NEGATIVE)

                            runCatching {
                                val mode = Registry.modes[context.arg("mode", StringArgumentType.word())]
                                if (mode != null) {
                                    mode.gameConstructor(players.last().world, players, difficulty).init()
                                } else {
                                    return@action source.locale().component("command.games.start.unknown_mode", color = Colors.NEGATIVE)
                                }
                            }.onFailure {
                                NeoBB.LOG.error("Could not start match!", it)
                                return@action source.locale().component("command.games.start.error", color = Colors.NEGATIVE)
                            }
                            return@action source.locale().component("command.games.start.starting", color = Colors.POSITIVE)
                        }
                    }
                }
            }
        }
        subcommand("stop") {
            argument("id", ExtendedArgumentTypes.valued { GameManager.GAMES.keys }.paper()) {
                action { context ->
                    val id = context.arg("id", StringArgumentType.word())
                    val game = GameManager[id] ?: return@action context.exec().locale().component("command.games.start.not_found", id, color = Colors.NEGATIVE)

                    game.end(true)
                    return@action context.exec().locale().component("command.games.stop.success", id, color = Colors.NEUTRAL)
                }
            }
        }
        subcommand("list") {
            action { context ->
                val source = context.exec()
                if (GameManager.GAMES.isEmpty())
                    return@action component("There are no games running.", Colors.NEUTRAL)

                source.sendMessage(component("Loaded Structures:"))
                for (game in GameManager.GAMES.values) {
                    source.sendMessage(component("==== ", Colors.EXTRA).append(component(game.id, Colors.ACCENT)).append(component(" ====", Colors.EXTRA)))
                    source.sendMessage(component("> Players: ", Colors.EXTRA).append(component("${game.players.size}/${game.initialPlayers.size}", Colors.SECONDARY)))
                    source.sendMessage(component("> Time: ", Colors.EXTRA).append(component(game.timeLeft.preciselyFormatted, Colors.SECONDARY)))
                    source.sendMessage(component("> Difficulty: ", Colors.EXTRA).append(component(game.difficulty.name, Colors.SECONDARY)))
                    source.sendMessage(component("> Played Turns: ", Colors.EXTRA).append(Component.text(game.history.size, Colors.SECONDARY)))
                    source.sendMessage(component("> Turning: ", Colors.EXTRA).append(component(game.currentPlayer().name(), Colors.SECONDARY)))
                }
                return@action null
            }
        }
    }

    val giveup = command("giveup") {
        requirePlayer()
        playerAction { context, player ->
            val neoPlayer = GameManager.player(player)
            if (neoPlayer != null && neoPlayer.game.started) {
                neoPlayer.game.eliminate(neoPlayer)
                return@playerAction player.locale().component("command.giveup.confirm", color = Colors.NEUTRAL)
            }
            return@playerAction player.locale().component("command.not_in_game", color = Colors.NEGATIVE)
        }
    }

    val structure = command("structure") {
        require("neobb.structures")
        subcommand("save") {
            argument("name", StringArgumentType.word()) {
                argument("world", ArgumentTypes.world()) {
                    argument("corner1", ArgumentTypes.blockPosition()) {
                        argument("corner2", ArgumentTypes.blockPosition()) {
                            action { context ->
                                val source = context.exec()
                                val name = context.arg("name", StringArgumentType.word())

                                if (StructureManager[name] != null) {
                                    source.sendMessage(component("Structure with name \"$name\" already exists!", Colors.NEUTRAL))
                                    source.sendMessage(component("You can remove it using \"/structure remove $name\"!", Colors.EXTRA))
                                    return@action null
                                }

                                val world = context.arg("world", ArgumentTypes.world())
                                val corner1 = context.arg("corner1", ArgumentTypes.blockPosition()).resolve(context.source).toLocation(world)
                                val corner2 = context.arg("corner2", ArgumentTypes.blockPosition()).resolve(context.source).toLocation(world)

                                val structure = Structure(name, world, corner1.toCord(), corner2.toCord())
                                structure.save()
                                StructureManager.add(structure)

                                return@action component("Successfully saved structure with name \"${structure.name}\".", Colors.POSITIVE)
                            }
                        }
                    }
                }
            }
        }
        subcommand("remove") {
            argument("name", ExtendedArgumentTypes.valued { StructureManager.LOADED.map { it.name } }.paper()) {
                action { context ->
                    val name = context.arg("name", String::class.java)
                    val structure = StructureManager[name]
                    if (structure == null)
                        return@action component("Could not find structure with name: $name", Colors.NEGATIVE)

                    StructureManager.remove(structure)
                    return@action component("Successfully removed structure with name: ${structure.name}", Colors.NEUTRAL)
                }
            }
        }
        subcommand("list") {
            action { context ->
                if (StructureManager.LOADED.isEmpty())
                    return@action component("There are no structures loaded.", Colors.NEUTRAL)

                context.feedback(component("Loaded Structures:"))
                return@action component(StructureManager.LOADED.joinToString("\n") { "- ${it.name} (${it.materials.size})" })
            }
        }
        subcommand("place") {
            argument("name", ExtendedArgumentTypes.valued { StructureManager.LOADED.map { it.name } }.paper()) {
                argument("location", ArgumentTypes.blockPosition()) {
                    action { context ->
                        val name = context.arg("name", String::class.java)
                        val location = context.arg("location", ArgumentTypes.blockPosition()).resolve(context.source).toLocation(context.source.location.world)

                        val structure = StructureManager[name]
                        if (structure == null)
                            return@action component("Could not find structure with name: $name", Colors.NEGATIVE)

                        val obstructed = structure.place(location, false)
                        return@action when {
                            obstructed >= 1.0 -> component("Could not place structure $name, as it was fully obstructed.", Colors.NEGATIVE)
                            obstructed > 0.0 -> component("Partially placed structure $name - ~$obstructed% were obstructed.", Colors.NEUTRAL)
                            else -> component("Successfully placed structure $name.", Colors.POSITIVE)
                        }
                    }
                }
            }
        }
    }

    val debug = command("debug") {
        require("neobb.debug")
        subcommand("effect") {
            subcommand("player-animation") {
                argument("player", ArgumentTypes.player()) {
                    argument("animation", ExtendedArgumentTypes.enum(PlayerAnimation::class.java).paper()) {
                        action { context ->
                            val animation = context.arg("animation", PlayerAnimation::class.java)
                            val target = context.arg("player", ArgumentTypes.player()).resolve(context.source).first()

                            animation.play(target, target.location.toCord() - Cord(4.0, 0.0, 4.0))
                            return@action component("Successfully played player-animation with name \"${animation.name}\".", Colors.POSITIVE)
                        }
                    }
                }
            }
            subcommand("place-particle") {
                argument("block", ArgumentTypes.blockPosition()) {
                    argument("particle", ExtendedArgumentTypes.enum(PlaceParticle::class.java).paper()) {
                        action { context ->
                            val world = context.source.location.world ?: Bukkit.getWorld("world")!!
                            val block = context.arg("block", ArgumentTypes.blockPosition()).resolve(context.source).toLocation(world)

                            context.arg("particle", PlaceParticle::class.java).spawn(block)
                            return@action component("Successfully shown place-particle with name \"${context.arg("particle", PlaceParticle::class.java).name}\".", Colors.POSITIVE)
                        }
                    }
                }
            }
        }
    }
}
