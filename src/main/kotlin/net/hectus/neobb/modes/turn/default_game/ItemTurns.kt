package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.toLocation
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.attribute.CounterFilter
import net.hectus.neobb.modes.turn.default_game.attribute.CounterbuffFunction
import net.hectus.neobb.modes.turn.default_game.attribute.EventFunction
import net.hectus.neobb.modes.turn.default_game.attribute.TurnClazz
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import kotlin.random.Random

abstract class ItemTurn(namespace: String) : Turn<ItemStack>(namespace) {
    override val event: TurnEvent = TurnEvent.CUSTOM
}

object TChorusFruit : ItemTurn("chorus_fruit"), EventFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 2

    override fun triggerEvent(exec: TurnExec<*>) {
        val location: Location = (exec.game.warp.bounds.low + Cord(Random.nextDouble(0.0, 9.0), 0.0, Random.nextDouble(0.0, 9.0))).toLocation(exec.game.world)
        location.pitch = (Random.nextFloat() * 180.0f) - 90.0f
        location.yaw = Random.nextFloat() * 360.0f
        exec.player.player.teleport(location)
    }
}

object TIronShovel : ItemTurn("iron_shovel"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 5

    override val counters: List<CounterFilter> = listOf(
        CounterFilter.of("dirt") { it.turn === TDirt },
        CounterFilter.of("cauldron") { it.turn === TCauldron },
        TurnClazz.HOT
    )
    override val buffs: List<Buff<*>> = listOf(ExtraTurn())
}
