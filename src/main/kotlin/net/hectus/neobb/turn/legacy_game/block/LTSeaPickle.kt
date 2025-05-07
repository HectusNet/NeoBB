package net.hectus.neobb.turn.legacy_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.function.AttackFunction
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.turn.default_game.block.BlockTurn
import org.bukkit.block.Block

class LTSeaPickle(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction, BuffFunction, NatureClazz {
    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn(2, Buff.BuffTarget.NEXT))
    }
}