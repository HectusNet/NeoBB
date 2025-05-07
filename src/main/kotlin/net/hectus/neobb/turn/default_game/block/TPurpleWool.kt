package net.hectus.neobb.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.function.AttackFunction
import org.bukkit.block.Block

class TPurpleWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction, NeutralClazz {
    override val cost: Int = 4

    override fun unusable(): Boolean {
        if (isDummy()) return true

        val history = player!!.game.history
        if (history.size < 5) return true

        return history.subList(history.size - 5, history.size).any { it is AttackFunction }
    }

    override fun apply() {
        player!!.game.win(player)
    }
}