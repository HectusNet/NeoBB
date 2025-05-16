package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ChancedBuff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Teleport
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.asCord
import org.bukkit.block.Block

class TRespawnAnchor(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction, HotClazz {
    override val cost: Int = 4

    override fun buffs(): List<Buff<*>> {
        return listOf(Teleport(player!!.game.history.lastOrNull()?.location()?.asCord() ?: Cord(0.0, 0.0, 0.0)), ChancedBuff(50.0, ExtraTurn()))
    }
}
