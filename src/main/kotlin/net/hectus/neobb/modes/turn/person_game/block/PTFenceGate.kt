package net.hectus.neobb.modes.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.block.BlockTurn
import net.hectus.neobb.modes.turn.person_game.categorization.CounterCategory
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

class PTFenceGate(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory {
    override fun item(): ItemStack = ItemStack(Material.OAK_FENCE_GATE)

    override fun counter(source: NeoPlayer, countered: Turn<*>) {
        super.counter(source, countered)
        countered.player!!.damage(countered.damage + 2.0)
    }
}
