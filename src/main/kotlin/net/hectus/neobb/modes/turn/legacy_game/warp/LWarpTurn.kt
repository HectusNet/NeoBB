package net.hectus.neobb.modes.turn.legacy_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.modes.turn.default_game.warp.WarpTurn
import net.hectus.neobb.player.NeoPlayer

abstract class LWarpTurn(data: PlacedStructure?, cord: Cord, name: String, player: NeoPlayer?) : WarpTurn(data, cord, "legacy-$name", player)
