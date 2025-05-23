package net.hectus.neobb.modes.turn.default_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.*
import net.hectus.neobb.player.NeoPlayer
import kotlin.reflect.KClass

class TAmethystWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : WarpTurn(data, cord, "amethyst", player) {
    override val staticStructure: StaticStructure = StaticStructures.Default.Warp.AMETHYST

    override val cost: Int = 4
    override val chance: Double = 20.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, WaterClazz::class, NatureClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}
