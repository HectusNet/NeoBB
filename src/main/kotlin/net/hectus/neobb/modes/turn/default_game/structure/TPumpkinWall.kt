package net.hectus.neobb.modes.turn.default_game.structure

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.modes.turn.default_game.attribute.function.BuffFunction
import net.hectus.neobb.player.NeoPlayer

class TPumpkinWall(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), BuffFunction, NatureClazz {
    override val cost: Int = 5
    override val staticStructure: StaticStructure = StaticStructures.Default.PUMPKIN_WALL

    override fun apply() {
        player!!.game.time = MinecraftTime.MIDNIGHT
    }

    override fun buffs(): List<Buff<*>> = listOf(Luck(10))
}
