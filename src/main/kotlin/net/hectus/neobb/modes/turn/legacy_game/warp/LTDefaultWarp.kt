package net.hectus.neobb.modes.turn.legacy_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.*
import net.hectus.neobb.player.NeoPlayer
import kotlin.reflect.KClass

class LTDefaultWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : LWarpTurn(data, cord, "default", player) {
    // Just anything that exists will be ignored anyways.
    override val staticStructure: StaticStructure = StaticStructures.Legacy.PUMPKIN_WALL

    override val chance: Double = 0.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, WaterClazz::class, NatureClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}
