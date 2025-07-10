package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.data.time.Time
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.component
import net.hectus.neobb.modes.turn.default_game.attribute.NeutralClazz
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class TTimeLimit(data: Time?, cord: Cord?, player: NeoPlayer?) : OtherTurn<Time>(data, cord, player), NeutralClazz {
    override fun apply() {
        player!!.sendMessage(player.locale().component("gameplay.info.too-slow.player", data.toString(), color = Colors.NEGATIVE))
        player.opponents(false).forEach { p ->
            p.sendMessage(p.locale().component("gameplay.info.too-slow." + (if (p === player.nextPlayer()) "next" else "others"), player.nextPlayer().name(), color = Colors.NEUTRAL))
        }
    }

    override fun item(): ItemStack = ItemStack(Material.CLOCK)
}
