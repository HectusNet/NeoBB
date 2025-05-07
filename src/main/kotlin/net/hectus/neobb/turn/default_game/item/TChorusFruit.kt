package net.hectus.neobb.turn.default_game.item

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.function.EventFunction
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

class TChorusFruit(data: ItemStack?, cord: Cord?, player: NeoPlayer?) : ItemTurn(data, cord, player), EventFunction, NeutralClazz {
    override val cost: Int = 2

    override fun triggerEvent() {
        val location: Location = player!!.game.warp.location().clone().add(Random.nextDouble(0.0, 9.0), 0.0, Random.nextDouble(0.0, 9.0))
        location.pitch = (Random.nextFloat() * 180.0f) - 90.0f
        location.yaw = Random.nextFloat() * 360.0f
        player.player.teleport(location)
    }
}