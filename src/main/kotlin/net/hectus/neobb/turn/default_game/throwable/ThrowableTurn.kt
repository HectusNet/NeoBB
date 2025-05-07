package net.hectus.neobb.turn.default_game.throwable

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.util.material
import org.bukkit.entity.Projectile
import org.bukkit.inventory.ItemStack

abstract class ThrowableTurn(data: Projectile?, cord: Cord?, player: NeoPlayer?) : Turn<Projectile>(data, cord, player) {
    override fun item(): ItemStack = ItemStack(this::class.material())
}
