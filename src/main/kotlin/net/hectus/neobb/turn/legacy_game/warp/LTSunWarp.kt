package net.hectus.neobb.turn.legacy_game.warp

import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import net.hectus.neobb.turn.default_game.attribute.clazz.Clazz
import net.hectus.neobb.turn.default_game.attribute.clazz.HotClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.NeutralClazz
import net.hectus.neobb.turn.default_game.attribute.clazz.WaterClazz
import net.hectus.neobb.util.Utilities
import org.bukkit.potion.PotionEffectType
import kotlin.reflect.KClass

class LTSunWarp(data: PlacedStructure?, player: NeoPlayer?) : LWarpTurn(data, "sun", player) {
    override val cost: Int = 4
    override val chance: Double = 30.0
    override val allows: List<KClass<out Clazz>> = listOf(NeutralClazz::class, HotClazz::class)
    override val temperature: Temperature = Temperature.HOT

    override fun apply() {
        Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.OPPONENTS)

        for (p in player!!.game.players) {
            val turns = p.inventory.dummyTurns
            for ((index, turn) in turns.withIndex()) {
                if (turn is WaterClazz)
                    player.inventory.clearSlot(index)
            }
        }
    }

    override fun canBePlayed(): Boolean = Utilities.isDaytime()
}