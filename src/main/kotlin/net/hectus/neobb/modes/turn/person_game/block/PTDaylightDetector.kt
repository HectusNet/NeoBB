package net.hectus.neobb.modes.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.modes.turn.default_game.block.BlockTurn
import net.hectus.neobb.modes.turn.person_game.categorization.CounterCategory
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class PTDaylightDetector(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory {
    override fun apply() {
        player!!.game.time = MinecraftTime.NOON
    }
}
