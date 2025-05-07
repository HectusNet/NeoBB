package net.hectus.neobb.turn.default_game.flower

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.turn.default_game.block.BlockTurn
import org.bukkit.block.Block

abstract class FlowerTurn(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction, NatureClazz {
    override fun goodChoice(player: NeoPlayer): Boolean {
        if (player.game.history.isEmpty()) return false
        val last = player.game.history.last()
        return super.goodChoice(player) && (last is TDirt || last is TFlowerPot)
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}
