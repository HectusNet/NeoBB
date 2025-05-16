package net.hectus.neobb.modes.turn.legacy_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Utilities
import kotlin.reflect.KClass

class LTBookWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : LWarpTurn(data, cord, "book", player) {
    override val staticStructure: StaticStructure = StaticStructures.Legacy.Warp.BOOK

    override val cost: Int = 4
    override val chance: Double = 20.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.NORMAL

    override fun apply() {
        Luck(35).apply(player!!)
    }

    override fun canBePlayed(): Boolean = Utilities.isWeekday()
}
