package net.hectus.neobb.game.mode

import net.hectus.neobb.game.BossBarGame
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.TPhantom
import net.hectus.neobb.modes.turn.default_game.attribute.AttackFunction
import net.hectus.neobb.modes.turn.default_game.attribute.CounterFunction
import net.hectus.neobb.modes.turn.default_game.attribute.DefenseFunction
import net.hectus.neobb.modes.turn.default_game.attribute.EventFunction
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import org.bukkit.World
import org.bukkit.entity.Player

abstract class HectusGame(world: World, bukkitPlayers: List<Player>, difficulty: GameDifficulty = GameDifficulty.NORMAL) : BossBarGame(world, bukkitPlayers, difficulty) {
    override fun executeTurn(exec: TurnExec<*>): Boolean {
        val player = exec.player
        val nextPlayer = player.targetPlayer()

        if (exec.turn is CounterFunction) {
            if (exec.turn.counterLogic(exec)) {
                info("Not applying counter from turn.")
                return false
            } else {
                info("Applied counter from turn.")
                player.sendMessage("gameplay.info.function.counter", color = Colors.POSITIVE)
            }
        }

        if (exec.turn is AttackFunction) {
            if (!nextPlayer.hasModifier(Modifiers.Player.Default.DEFENDED) || exec.turn === TPhantom) {
                info("Applying attack from turn.")
                player.sendMessage("gameplay.info.function.attack", color = Colors.POSITIVE)
                nextPlayer.addModifier(Modifiers.Player.Default.ATTACKED)
            } else {
                info("Not applying attack from turn, due to defense.")
                player.sendMessage("gameplay.info.function.attack.defended", color = Colors.NEGATIVE)
            }
        }

        val buffs = exec.turn.buffs
        if (buffs.isNotEmpty()) {
            info("Applying ${buffs.size} buffs from turn.")
            buffs.forEach { it(player) }
        }

        if (exec.turn is DefenseFunction) {
            info("Applying defense from turn.")
            if (player.hasModifier(Modifiers.Player.Default.ATTACKED)) {
                player.sendMessage("gameplay.info.function.defense.attacked", color = Colors.NEGATIVE)
            } else {
                player.sendMessage("gameplay.info.function.defense", color = Colors.POSITIVE)
                exec.turn.applyDefense(exec)
            }
        }

        if (exec.turn is EventFunction) {
            info("Triggering event from turn.")
            exec.turn.triggerEvent(exec)
        }

        return true
    }
}
