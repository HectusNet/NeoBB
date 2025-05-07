package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.*
import kotlin.reflect.KClass

class LTRedstoneWarp(data: PlacedStructure?, player: NeoPlayer?) : LWarpTurn(data, "redstone", player) {
    override val cost: Int = 4
    override val chance: Double = 65.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, HotClazz::class, ColdClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.NORMAL

    override fun apply() {
        Luck(5).apply(player!!)
    }
}