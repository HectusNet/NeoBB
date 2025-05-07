package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.function.AttackFunction
import org.bukkit.block.Block

class TMagmaBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction, HotClazz {
    override val cost: Int = 4
    override val requiresUsageGuide: Boolean = true
}