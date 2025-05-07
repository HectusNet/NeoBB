package net.hectus.neobb.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.block.BlockTurn
import net.hectus.neobb.turn.person_game.categorization.AttackCategory
import net.hectus.neobb.turn.person_game.warp.PTVillagerWarp
import org.bukkit.block.Block
import kotlin.reflect.KClass

class PTDiamondBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override val damage: Double
        get() = if (player!!.game.warp is PTVillagerWarp) 2.0 else 4.0

    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTLightBlueCarpet::class, PTNoteBlock::class)
    }
}