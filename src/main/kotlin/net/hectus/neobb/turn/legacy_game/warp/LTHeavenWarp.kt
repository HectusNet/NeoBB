package net.hectus.neobb.turn.legacy_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.structure.StaticStructure
import net.hectus.neobb.structure.StaticStructures
import net.hectus.neobb.turn.default_game.attribute.clazz.*
import kotlin.reflect.KClass

class LTHeavenWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : LWarpTurn(data, cord, "heaven", player) {
    override val staticStructure: StaticStructure = StaticStructures.Legacy.Warp.HEAVEN

    override val cost: Int = 4
    override val chance: Double = 100.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, ColdClazz::class, NatureClazz::class, SupernaturalClazz::class)
    override val temperature: Temperature = Temperature.NORMAL

    override fun apply() {
        Luck(20).apply(player!!)
        Luck(20, Buff.BuffTarget.OPPONENTS).apply(player)
    }
}
