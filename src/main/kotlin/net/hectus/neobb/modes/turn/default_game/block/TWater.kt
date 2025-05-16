package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Waterlogged
import org.bukkit.inventory.ItemStack

class TWater(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction, WaterClazz {
    override val cost: Int = 3

    override fun item(): ItemStack = ItemStack(Material.WATER_BUCKET)

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("waterloggable") { it is BlockTurn && it.data?.blockData is Waterlogged })
    }
}
