package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.*
import kotlin.reflect.KClass

class LTHeavenWarp(data: PlacedStructure?, player: NeoPlayer?) : LWarpTurn(data, "heaven", player) {
    override val cost: Int = 4
    override val chance: Double = 100.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, ColdClazz::class, NatureClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.NORMAL

    override fun apply() {
        Luck(20).apply(player!!)
        Luck(20, Buff.BuffTarget.OPPONENTS).apply(player)
    }
}