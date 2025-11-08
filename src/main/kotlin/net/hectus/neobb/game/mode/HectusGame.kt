package net.hectus.neobb.game.mode

import net.hectus.neobb.game.BossBarGame
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.TPhantom
import net.hectus.neobb.modes.turn.default_game.attribute.AttackFunction
import net.hectus.neobb.modes.turn.default_game.attribute.CounterFunction
import net.hectus.neobb.modes.turn.default_game.attribute.DefenseFunction
import net.hectus.neobb.modes.turn.default_game.attribute.EventFunction
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.*
import org.bukkit.World
import org.bukkit.entity.Player

abstract class HectusGame(world: World, bukkitPlayers: List<Player>, difficulty: GameDifficulty = GameDifficulty.NORMAL) : BossBarGame(world, bukkitPlayers, difficulty) {
    override fun executeTurn(exec: TurnExec<*>): Boolean {
        val player = exec.player

        if (exec.turn is CounterFunction) {
            if (counterFunc(exec, player))
                return false
        }

        if (exec.turn is AttackFunction)
            attackFunc(exec, player)

        val buffs = exec.turn.buffs
        if (buffs.isNotEmpty()) {
            info("Applying ${buffs.size} buffs from turn.")
            buffs.forEach { it(player) }
        }

        if (exec.turn is DefenseFunction)
            defenseFunc(exec, player)

        if (exec.turn is EventFunction) {
            info("Triggering event from turn.")
            exec.turn.triggerEvent(exec)
        }

        return true
    }

    private fun counterFunc(exec: TurnExec<*>, player: NeoPlayer): Boolean {
        if ((exec.turn as CounterFunction).counterLogic(exec)) {
            info("Not applying counter from turn.")
            return true
        }

        info("Applied counter from turn.")
        player.sendMessage("gameplay.info.function.counter", color = Colors.POSITIVE)
        return false
    }

    private fun attackFunc(exec: TurnExec<*>, player: NeoPlayer) {
        if (exec.turn !is AttackFunction) return

        val nextPlayer = player.targetPlayer()

        if (nextPlayer.standingHeight - 0.5 > player.standingHeight && (1.0 - Constants.ATTACK_SUCCESS_CHANCE).lossChance(player.luck)) {
            info("Not applying attack from turn, due to high-ground.")
            player.sendMessage("gameplay.info.function.attack.high-ground", color = Colors.NEGATIVE)
            return
        }

        if (nextPlayer.hasModifier(Modifiers.Player.Default.DEFENDED) && exec.turn !== TPhantom) {
            info("Not applying attack from turn, due to defense.")
            player.sendMessage("gameplay.info.function.attack.defended", color = Colors.NEGATIVE)
            return
        }

        if (Constants.ATTACK_SUCCESS_CHANCE.luckChance(player.luck)) {
            info("Applying attack from turn.")
            player.sendMessage("gameplay.info.function.attack", color = Colors.POSITIVE)
            nextPlayer.addModifier(Modifiers.Player.Default.ATTACKED)
        } else {
            info("Not applying attack from turn, due to luck.")
            player.sendMessage("gameplay.info.function.attack.unlucky", color = Colors.NEGATIVE)
        }
        return
    }

    private fun defenseFunc(exec: TurnExec<*>, player: NeoPlayer) {
        if (player.hasModifier(Modifiers.Player.Default.ATTACKED)) {
            info("Not applying defense from turn, due to previous attack.")
            player.sendMessage("gameplay.info.function.defense.attacked", color = Colors.NEGATIVE)
            return
        }

        info("Applying defense from turn.")
        player.sendMessage("gameplay.info.function.defense", color = Colors.POSITIVE)
        (exec.turn as DefenseFunction).applyDefense(exec)
        return
    }
}
