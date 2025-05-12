package net.hectus.neobb.turn.legacy_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.warp.WarpTurn

abstract class LWarpTurn(data: PlacedStructure?, cord: Cord, name: String, player: NeoPlayer?) : WarpTurn(data, cord, "legacy-$name", player)
