package net.hectus.neobb.turn.default_game.mob

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.util.camelToSnake
import net.hectus.neobb.util.counterFilterName
import net.hectus.neobb.util.enumValueNoCase
import org.bukkit.Material
import org.bukkit.entity.LivingEntity
import org.bukkit.inventory.ItemStack

abstract class MobTurn<T : LivingEntity>(data: T?, cord: Cord?, player: NeoPlayer?) : Turn<T>(data, cord, player) {
    override fun item(): ItemStack = ItemStack(enumValueNoCase<Material>((this::class.simpleName ?: "unknown").counterFilterName().camelToSnake() + "_SPAWN_EGG"))
}
