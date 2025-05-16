package net.hectus.neobb.modes.turn.legacy_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.*
import net.hectus.neobb.player.NeoPlayer
import kotlin.reflect.KClass

class LTRedstoneWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : LWarpTurn(data, cord, "redstone", player) {
    override val staticStructure: StaticStructure = StaticStructures.Legacy.Warp.REDSTONE

    override val cost: Int = 4
    override val chance: Double = 65.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, HotClazz::class, ColdClazz::class, RedstoneClazz::class)
    override val temperature: Temperature = Temperature.NORMAL

    override fun apply() {
        Luck(5).apply(player!!)
    }
}
