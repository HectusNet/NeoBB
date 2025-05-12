package net.hectus.neobb.turn.person_game.structure

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.StaticStructure
import net.hectus.neobb.structure.StaticStructures
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.hectus.neobb.turn.person_game.categorization.ArmorCategory
import net.hectus.neobb.turn.person_game.other.PTArmorStand
import kotlin.reflect.KClass

class PTCandleCircle(data: PlacedStructure?, cord: Cord?, player: NeoPlayer?) : StructureTurn(data, cord, player), ArmorCategory {
    override val staticStructure: StaticStructure = StaticStructures.Person.CANDLE_CIRCLE

    override fun armor(): Int = 3

    override fun counteredBy(): List<KClass<out Turn<*>>> = listOf(PTArmorStand::class)
}
