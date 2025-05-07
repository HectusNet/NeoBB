package net.hectus.neobb.game

import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.attribute.function.*
import net.hectus.neobb.turn.default_game.mob.TPhantom
import net.hectus.neobb.util.Modifiers
import org.bukkit.World
import org.bukkit.entity.Player

abstract class HectusGame(world: World, bukkitPlayers: List<Player>, difficulty: GameDifficulty = GameDifficulty.NORMAL) : BossBarGame(world, bukkitPlayers, difficulty) {
    override fun executeTurn(turn: Turn<*>): Boolean {
        if (turn is CounterFunction) {
            if (turn.counterLogic(turn)) {
                info("Not applying counter from turn.")
                return false
            } else {
                info("Applying counter from turn.")
            }
        }

        if (turn is AttackFunction) {
            if (!(nextPlayer().hasModifier(Modifiers.Player.Default.DEFENDED) || turn is TPhantom)) {
                info("Applying attack from turn.")
                nextPlayer().addModifier(Modifiers.Player.Default.ATTACKED)
            } else {
                info("Not applying attack from turn, due to defense.")
            }
        }

        if (turn is BuffFunction) {
            val buffs = turn.buffs()
            info("Applying ${buffs.size} buffs from turn.")
            buffs.forEach { it.apply(turn.player!!) }
        }

        if (turn is DefenseFunction) {
            info("Applying defense from turn.")
            turn.applyDefense()
        }

        if (turn is EventFunction) {
            info("Triggering event from turn.")
            turn.triggerEvent()
        }

        return true
    }
}
