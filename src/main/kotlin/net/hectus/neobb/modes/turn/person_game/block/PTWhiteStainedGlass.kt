package net.hectus.neobb.modes.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.modes.turn.default_game.block.BlockTurn
import net.hectus.neobb.modes.turn.person_game.categorization.DefensiveCategory
import net.hectus.neobb.modes.turn.person_game.warp.PTSnowWarp
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.block.Block

class PTWhiteStainedGlass(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), DefensiveCategory {
    override fun apply() {
        player!!.heal(if (player.game.warp is PTSnowWarp) 2.0 else 5.0)
    }

    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
    }

    override fun unusable(): Boolean {
        return player!!.game.time == MinecraftTime.MIDNIGHT
    }
}
