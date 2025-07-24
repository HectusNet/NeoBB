package net.hectus.neobb.modes.turn

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.toLocation
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.Location

data class TurnExec<T>(
    val turn: Turn<T>,
    val player: NeoPlayer,
    val cord: Cord?,
    val data: T,
    val meta: MutableMap<String, Any?> = mutableMapOf(),
) {
    val location: Location? = cord?.toLocation(player.game.world)

    val game = player.game
    val hist = game.history
}
