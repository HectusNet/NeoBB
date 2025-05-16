package net.hectus.neobb.modes.turn.person_game.other

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.other.OtherTurn
import net.hectus.neobb.modes.turn.person_game.categorization.DefensiveCounterCategory
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.inventory.ItemStack

class PTArmorStand(data: ArmorStand?, cord: Cord?, player: NeoPlayer?) : OtherTurn<ArmorStand>(data, cord, player), DefensiveCounterCategory {
    override fun item(): ItemStack = ItemStack(Material.ARMOR_STAND)
}
