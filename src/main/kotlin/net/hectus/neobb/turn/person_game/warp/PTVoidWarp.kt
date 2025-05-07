package net.hectus.neobb.turn.person_game.warp

import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.structure.PlacedStructure
import org.bukkit.potion.PotionEffectType

class PTVoidWarp(data: PlacedStructure?, player: NeoPlayer?) : PWarpTurn(data, "void", player) {
    override val chance: Double = 20.0

    override fun apply() {
        player!!.game.players.forEach { it.inventory.removeRandom() }
        Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.OPPONENTS).apply(player)

        player.game.turnScheduler.runTaskLater(ScheduleID.BLINDNESS, 3) {
            player.opponents().forEach { it.player.removePotionEffect(PotionEffectType.BLINDNESS) }
        }
    }
}
