package net.hectus.neobb.modes.turn.default_game.item

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.material
import org.bukkit.inventory.ItemStack

abstract class ItemTurn(data: ItemStack?, cord: Cord?, player: NeoPlayer?) : Turn<ItemStack>(data, cord, player) {
    override fun item(): ItemStack = ItemStack(this::class.material())
}
