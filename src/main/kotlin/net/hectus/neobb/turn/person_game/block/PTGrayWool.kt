package net.hectus.neobb.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.block.BlockTurn
import net.hectus.neobb.turn.person_game.categorization.AttackCategory
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