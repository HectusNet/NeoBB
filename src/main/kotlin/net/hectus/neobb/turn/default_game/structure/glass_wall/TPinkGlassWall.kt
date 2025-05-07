package net.hectus.neobb.turn.default_game.structure.glass_wall

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz

class TPinkGlassWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : GlassWallTurn(data, cord, player), SupernaturalClazz
