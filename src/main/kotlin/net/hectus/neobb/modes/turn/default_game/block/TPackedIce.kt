package net.hectus.neobb.modes.turn.default_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.AttackFunction
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TPackedIce(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction, ColdClazz {
    override val cost: Int = 3
}
