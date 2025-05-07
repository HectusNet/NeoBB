package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.*
import kotlin.reflect.KClass

class LTIceWarp(data: PlacedStructure?, player: NeoPlayer?) : LWarpTurn(data, "ice", player) {
    override val cost: Int = 4
    override val chance: Double = 69.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, ColdClazz::class, WaterClazz::class, NatureClazz::class)
    override val temperature: Temperature = Temperature.COLD

    override fun apply() {
        Luck(5).apply(player!!)
    }
}