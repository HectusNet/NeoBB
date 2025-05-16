package net.hectus.neobb.modes.turn.legacy_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.*
import net.hectus.neobb.player.NeoPlayer
import kotlin.reflect.KClass

class LTUnderwaterWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : LWarpTurn(data, cord, "underwater", player) {
    override val staticStructure: StaticStructure = StaticStructures.Legacy.Warp.UNDERWATER

    override val cost: Int = 4
    override val chance: Double = 73.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, ColdClazz::class, WaterClazz::class, NatureClazz::class)
    override val temperature: Temperature = Temperature.NORMAL

    override fun apply() {
        Luck(8).apply(player!!)
    }
}
