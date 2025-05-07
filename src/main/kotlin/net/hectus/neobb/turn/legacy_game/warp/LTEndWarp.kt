package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.*
import net.hectus.neobb.turn.default_game.throwable.TEnderPearl
import kotlin.reflect.KClass

class LTEndWarp(data: PlacedStructure?, player: NeoPlayer?) : LWarpTurn(data, "end", player) {
    override val cost: Int = 4
    override val chance: Double = 60.0
    override val allows: List<KClass<out Clazz>> = listOf(ColdClazz::class, WaterClazz::class, RedstoneClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.COLD

    override fun apply() {
        Luck(10).apply(player!!)
        player.inventory.add(TEnderPearl(null, null, null))
    }
}