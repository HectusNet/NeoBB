package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.SupernaturalClazz
import net.hectus.neobb.util.Utilities
import kotlin.reflect.KClass

class LTBookWarp(data: PlacedStructure?, player: NeoPlayer?) : LWarpTurn(data, "book", player) {
    override val cost: Int = 4
    override val chance: Double = 20.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.NORMAL

    override fun apply() {
        Luck(35).apply(player!!)
    }

    override fun canBePlayed(): Boolean = Utilities.isWeekday()
}