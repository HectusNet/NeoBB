package net.hectus.neobb.modes.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.block.BlockTurn
import net.hectus.neobb.modes.turn.person_game.categorization.WinConCategory
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block
import kotlin.reflect.KClass

class PTVerdantFroglight(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), WinConCategory {
    override val damage: Double = 4.0

    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTFenceGate::class)
    }

    override fun unusable(): Boolean {
        return player!!.nextPlayer().health > 4.0
    }
}
