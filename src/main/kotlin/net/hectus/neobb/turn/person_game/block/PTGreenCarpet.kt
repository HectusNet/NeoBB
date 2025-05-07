package net.hectus.neobb.turn.person_game.block

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.default_game.block.BlockTurn
import net.hectus.neobb.turn.person_game.categorization.CounterCategory
import org.bukkit.block.Block

class PTGreenCarpet(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory