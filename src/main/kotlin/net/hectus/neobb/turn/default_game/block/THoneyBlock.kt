package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.CounterFilter
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.turn.default_game.attribute.function.CounterFunction
import org.bukkit.block.Block

class THoneyBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction, NatureClazz {
    override val cost: Int = 5

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NatureClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(RedstoneClazz::class))
    }
}