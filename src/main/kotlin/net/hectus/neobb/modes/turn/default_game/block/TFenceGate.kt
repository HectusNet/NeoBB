package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.CounterattackFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

class TFenceGate(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction, RedstoneClazz {
    override val cost: Int = 4

    override fun item(): ItemStack = ItemStack(Material.OAK_FENCE_GATE)

    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(HotClazz::class))
    }
}
