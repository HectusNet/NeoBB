package net.hectus.neobb.turn.legacy_game.structure.glass_wall

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.structure.StructureManager
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.structure.glass_wall.GlassWallTurn
import net.hectus.neobb.util.Modifiers

class LTGlassWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : GlassWallTurn(data, cord, player), NeutralClazz {
    override val referenceStructure: Structure = StructureManager["glass_wall"]!!

    override fun color(): String = ""

    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}