package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.ColdClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import kotlin.reflect.KClass

class LTAmethystWarp(data: PlacedStructure?, player: NeoPlayer?) : LWarpTurn(data, "amethyst", player) {
    override val cost: Int = 4
    override val chance: Double = 30.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, ColdClazz::class)
    override val temperature: Temperature = Temperature.NORMAL

    override fun apply() {
        Luck(20).apply(player!!)
    }
}