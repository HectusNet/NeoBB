package net.hectus.neobb.modes.turn.default_game.attribute

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.component
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Constants
import net.hectus.neobb.util.Modifiers

interface Function

interface AttackFunction : Function

interface BuffFunction : Function

interface CounterattackFunction : CounterFunction, AttackFunction

interface CounterbuffFunction : CounterFunction, BuffFunction

interface CounterFunction : Function {
    fun counterLogic(turn: TurnExec<*>): Boolean {
        if (turn.hist.isEmpty()) return true
        val last: TurnExec<*> = turn.hist.last()

        val luckLoss = if (this is CounterattackFunction) Constants.COUNTERATTACK_LUCK_LOSS.random() else Constants.COUNTER_LUCK_LOSS.random()
        turn.player.sendMessage(turn.player.locale().component("gameplay.info.function.counter${if (this is CounterattackFunction) "attack" else ""}.luck", luckLoss.toString(), color = Colors.NEGATIVE))
        turn.player.luck -= luckLoss

        if (!turn.game.difficulty.blockPositionRules || last.cord?.plus(Cord(0.0, 1.0, 0.0)) == turn.cord) { // Only check if it's a hard or champ difficulty.
            if (!turn.game.difficulty.completeRules || (this as Turn<*>).counters.any { it.doCounter(last) }) {
                counter(turn.player, last)
            } else {
                turn.player.sendMessage(turn.player.locale().component("gameplay.info.function.counter.wrong", color = Colors.NEGATIVE))
                return true
            }
        }
        return false
    }

    fun counter(source: NeoPlayer, countered: TurnExec<*>) {
        source.removeModifier(Modifiers.Player.Default.ATTACKED)
    }
}

interface DefenseFunction : Function {
    fun applyDefense(exec: TurnExec<*>)
}

interface EventFunction : Function {
    fun triggerEvent(exec: TurnExec<*>)
}

interface WarpFunction : Function
