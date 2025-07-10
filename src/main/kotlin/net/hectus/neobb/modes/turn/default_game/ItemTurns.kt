package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.material
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

abstract class ItemTurn(data: ItemStack?, cord: Cord?, player: NeoPlayer?) : Turn<ItemStack>(data, cord, player) {
    override fun item(): ItemStack = ItemStack(this::class.material())
}

class TChorusFruit(data: ItemStack?, cord: Cord?, player: NeoPlayer?) : ItemTurn(data, cord, player), EventFunction, NeutralClazz {
    override val cost: Int = 2
    override fun triggerEvent() {
        val location: Location = player!!.game.warp.location().clone().add(Random.nextDouble(0.0, 9.0), 0.0, Random.nextDouble(0.0, 9.0))
        location.pitch = (Random.nextFloat() * 180.0f) - 90.0f
        location.yaw = Random.nextFloat() * 360.0f
        player.player.teleport(location)
    }
}

class TIronShovel(data: ItemStack?, cord: Cord?, player: NeoPlayer?) : ItemTurn(data, cord, player), CounterbuffFunction, NeutralClazz {
    override val cost: Int = 5
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("dirt") { it is TDirt }, CounterFilter.of("cauldron") { it is TCauldron }, CounterFilter.clazz(HotClazz::class))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn())
    }
}
