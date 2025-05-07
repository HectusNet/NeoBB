package net.hectus.neobb.turn.default_game.warp

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.Structure
import net.hectus.neobb.structure.StructureManager
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NatureClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import kotlin.reflect.KClass

class TDefaultWarp(data: PlacedStructure?, player: NeoPlayer?) : WarpTurn(data, "default", player) {
    override val chance: Double = 0.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, WaterClazz::class, NatureClazz::class)
    override val temperature: Temperature = Temperature.NORMAL

    // Just anything that exists will be ignored anyways.
    override val referenceStructure: Structure = StructureManager["pumpkin_wall"]!!

    override fun canBePlayed(): Boolean = false
    override fun apply() {}
}
