package net.hectus.neobb.modes.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.block.BlockTurn
import net.hectus.neobb.modes.turn.person_game.categorization.UtilityCategory
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class PTSeaLantern(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), UtilityCategory {
    override fun apply() {
        player!!.addArmor(1.0)
    }
}
