package net.hectus.neobb.modes.turn.legacy_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.player.NeoPlayer
import kotlin.reflect.KClass

class LTNetherWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : LWarpTurn(data, cord, "nether", player) {
    override val staticStructure: StaticStructure = StaticStructures.Legacy.Warp.NETHER

    override val cost: Int = 4
    override val chance: Double = 80.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, HotClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.HOT
}
