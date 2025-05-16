package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.component
import org.bukkit.block.Block

class TSeaLantern(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction, WaterClazz {
    override val cost: Int = 7

    override fun apply() {
        player!!.addModifier(Modifiers.Player.REVIVE)
        player.sendMessage(player.locale().component("gameplay.info.revive.start", color = Colors.POSITIVE))
        player.game.turnScheduler.runTaskLater(ScheduleID.REVIVE, 3) {
            player.removeModifier(Modifiers.Player.REVIVE)
            player.sendMessage(player.locale().component("gameplay.info.revive.end", color = Colors.NEGATIVE))
        }
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}
