package net.hectus.neobb.modes.turn.legacy_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.AttackFunction
import net.hectus.neobb.modes.turn.default_game.block.BlockTurn
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class LTPurpleWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction, NeutralClazz {
    override val cost: Int = 15

    override fun unusable(): Boolean {
        return isDummy() || player!!.game.history.size < 10
    }

    override fun apply() {
        player!!.game.win(player)
    }
}
