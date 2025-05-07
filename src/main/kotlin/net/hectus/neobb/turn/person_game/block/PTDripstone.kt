package net.hectus.neobb.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.block.BlockTurn
import net.hectus.neobb.turn.person_game.categorization.CounterCategory
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

class PTDripstone(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory {
    override fun item(): ItemStack = ItemStack(Material.POINTED_DRIPSTONE)
}