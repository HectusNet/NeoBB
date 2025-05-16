package net.hectus.neobb.modes.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.modes.turn.default_game.block.BlockTurn
import net.hectus.neobb.modes.turn.person_game.categorization.UtilityCategory
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class PTGlowstone(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), UtilityCategory {
    override val damage: Double = 2.0

    override fun apply() {
        ExtraTurn(if (Randomizer.boolByChance(25.0)) 2 else 1).apply(player!!)
    }
}
