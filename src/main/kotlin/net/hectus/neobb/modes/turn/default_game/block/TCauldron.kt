package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.AttackFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TCauldron(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction, NeutralClazz {
    override val cost: Int = 2
}
