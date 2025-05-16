package net.hectus.neobb.modes.turn.default_game.flower

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.modes.turn.ComboTurn
import net.hectus.neobb.modes.turn.default_game.block.BlockTurn
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

class TDirt(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), ComboTurn {
    override val cost: Int = 2
}
