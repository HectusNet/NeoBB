package net.hectus.neobb.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.block.BlockTurn
import net.hectus.neobb.turn.person_game.categorization.AttackCategory
import org.bukkit.block.Block
import kotlin.reflect.KClass

class PTOrangeWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override val damage: Double = 2.0

    override fun apply() {
        Luck(10).apply(player!!)
    }

    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTGreenCarpet::class, PTBarrel::class)
    }
}