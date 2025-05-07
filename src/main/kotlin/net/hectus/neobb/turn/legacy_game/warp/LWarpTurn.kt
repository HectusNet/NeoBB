package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.warp.WarpTurn

abstract class LWarpTurn(data: PlacedStructure?, name: String, player: NeoPlayer?) : WarpTurn(data, "legacy-$name", player)