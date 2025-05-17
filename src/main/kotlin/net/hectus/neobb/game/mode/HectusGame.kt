package net.hectus.neobb.game.mode

import net.hectus.neobb.game.BossBarGame
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.attribute.function.*
import net.hectus.neobb.modes.turn.default_game.mob.TPhantom
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import org.bukkit.World
import org.bukkit.entity.Player

abstract class HectusGame(world: World, bukkitPlayers: List<Player>, difficulty: GameDifficulty = GameDifficulty.NORMAL) : BossBarGame(world, bukkitPlayers, difficulty) {
    override fun executeTurn(turn: Turn<*>): Boolean {
        val player = turn.player!!
        val nextPlayer = player.nextPlayer()

        if (turn is CounterFunction) {
            if (turn.counterLogic(turn)) {
                info("Not applying counter from turn.")
                return false
            } else {
                info("Applied counter from turn.")
                player.sendMessage("gameplay.info.function.counter", color = Colors.POSITIVE)
            }
        }

        if (turn is AttackFunction) {
            if (!nextPlayer.hasModifier(Modifiers.Player.Default.DEFENDED) || turn is TPhantom) {
                info("Applying attack from turn.")
                player.sendMessage("gameplay.info.function.attack", color = Colors.POSITIVE)
                nextPlayer.addModifier(Modifiers.Player.Default.ATTACKED)
            } else {
                info("Not applying attack from turn, due to defense.")
                player.sendMessage("gameplay.info.function.attack.defended", color = Colors.NEGATIVE)
            }
        }

        if (turn is BuffFunction) {
            val buffs = turn.buffs()
            info("Applying ${buffs.size} buffs from turn.")
            buffs.forEach { it.apply(player) }
        }

        if (turn is DefenseFunction) {
            info("Applying defense from turn.")
            player.sendMessage("gameplay.info.function.defense", color = Colors.POSITIVE)
            turn.applyDefense()
        }

        if (turn is EventFunction) {
            info("Triggering event from turn.")
            turn.triggerEvent()
        }

        return true
    }
}
