package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.block.Block

class TStonecutter(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction, NeutralClazz {
    override val cost: Int = 4

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("walls") { it::class.simpleName!!.endsWith("Wall") })
    }

    override fun counter(source: NeoPlayer, countered: Turn<*>) {
        super.counter(source, countered)
        source.opponents().forEach { it.removeModifier(Modifiers.Player.Default.DEFENDED) }
    }
}
