package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.data.time.Time
import com.marcpg.libpg.util.component
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.attribute.TurnClazz
import net.hectus.neobb.util.Colors
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object TTimeLimit : OtherTurn<Time>("time_limit") {
    override val mode: String = "any"
    override val event: TurnEvent = TurnEvent.NONE
    override val clazz: TurnClazz = TurnClazz.NEUTRAL
    override val mainItem: ItemStack = ItemStack.of(Material.CLOCK)

    override fun apply(exec: TurnExec<Time>) {
        exec.player.sendMessage(exec.player.locale().component("gameplay.info.too-slow.player", exec.data.toString(), color = Colors.NEGATIVE))
        exec.player.opponents(false).forEach { p ->
            p.sendMessage(p.locale().component("gameplay.info.too-slow." + (if (p == exec.player.targetPlayer()) "next" else "others"), exec.player.targetPlayer().name(), color = Colors.NEUTRAL))
        }
    }
}
