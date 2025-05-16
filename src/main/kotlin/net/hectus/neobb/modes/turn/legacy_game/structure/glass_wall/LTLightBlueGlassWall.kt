package net.hectus.neobb.modes.turn.legacy_game.structure.glass_wall

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.structure.glass_wall.GlassWallTurn
import net.hectus.neobb.player.NeoPlayer

class LTLightBlueGlassWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : GlassWallTurn(data, cord, player), NeutralClazz {
    override val staticStructure: StaticStructure = StaticStructures.Legacy.GlassWall.LIGHT_BLUE
}
