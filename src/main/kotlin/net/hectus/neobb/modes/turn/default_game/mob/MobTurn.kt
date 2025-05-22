package net.hectus.neobb.modes.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.util.camelToSnake
import net.hectus.util.counterFilterName
import net.hectus.util.enumValueNoCase
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

abstract class MobTurn<T : LivingEntity>(data: T?, cord: Cord?, player: NeoPlayer?) : Turn<T>(data, cord, player) {
    override fun item(): ItemStack = ItemStack(enumValueNoCase<Material>((this::class.simpleName ?: "unknown").counterFilterName().camelToSnake() + "_SPAWN_EGG"))
}
