package net.hectus.neobb.modes.turn.default_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.*
import net.hectus.neobb.player.NeoPlayer
import kotlin.reflect.KClass

class TVoidWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "void", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.VOID

    override val cost: Int = 4
    override val chance: Double = 10.0
    override val allows: List<KClass<out Clazz>> = listOf(ColdClazz::class, NeutralClazz::class, WaterClazz::class, RedstoneClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.COLD
}
