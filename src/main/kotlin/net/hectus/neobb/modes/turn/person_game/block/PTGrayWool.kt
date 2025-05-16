package net.hectus.neobb.modes.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.block.BlockTurn
import net.hectus.neobb.modes.turn.person_game.categorization.AttackCategory
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block
import kotlin.reflect.KClass

class PTGrayWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override fun apply() {
        player!!.nextPlayer().damage(2.0, true)
    }

    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTNoteBlock::class, PTGoldBlock::class)
    }
}
