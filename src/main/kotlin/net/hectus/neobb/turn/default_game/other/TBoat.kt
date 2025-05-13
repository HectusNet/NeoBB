package net.hectus.neobb.turn.default_game.other

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.turn.default_game.attribute.function.EventFunction
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.bukkitRunTimer
import org.bukkit.Material
import org.bukkit.entity.Boat
import org.bukkit.inventory.ItemStack

class TBoat(data: Boat?, cord: Cord?, player: NeoPlayer?) : OtherTurn<Boat>(data, cord, player), EventFunction, WaterClazz {
    override val cost: Int = 3

    override fun item(): ItemStack = ItemStack(Material.OAK_BOAT)

    override fun triggerEvent() {
        player!!.nextPlayer().addModifier(Modifiers.Player.Default.BOAT_DAMAGE)
        bukkitRunTimer(0, 20) { r ->
            if (player.nextPlayer().hasModifier(Modifiers.Player.Default.BOAT_DAMAGE)) {
                player.nextPlayer().damage(1.0)
            } else {
                r.cancel()
            }
        }
    }
}
