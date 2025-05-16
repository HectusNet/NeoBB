package net.hectus.neobb.modes.turn.legacy_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.attribute.clazz.*
import net.hectus.neobb.modes.turn.default_game.throwable.TEnderPearl
import net.hectus.neobb.player.NeoPlayer
import kotlin.reflect.KClass

class LTEndWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : LWarpTurn(data, cord, "end", player) {
    override val staticStructure: StaticStructure = StaticStructures.Legacy.Warp.END

    override val cost: Int = 4
    override val chance: Double = 60.0
    override val allows: List<KClass<out Clazz>> = listOf(ColdClazz::class, WaterClazz::class, RedstoneClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.COLD

    override fun apply() {
        Luck(10).apply(player!!)
        player.inventory.add(TEnderPearl(null, null, null))
    }
}
