package net.hectus.neobb.modes.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.default_game.block.BlockTurn
import net.hectus.neobb.modes.turn.person_game.categorization.BuffCategory
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class PTCake(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffCategory {
    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(20))
    }
}
