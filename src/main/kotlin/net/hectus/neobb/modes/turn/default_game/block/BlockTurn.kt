package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.util.material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

abstract class BlockTurn(data: Block?, cord: Cord?, player: NeoPlayer?) : Turn<Block>(data, cord?.add(Cord(0.5, 0.5, 0.5)), player) {
    override fun item(): ItemStack = ItemStack(this::class.material())
}
