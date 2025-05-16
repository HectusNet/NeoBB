package net.hectus.neobb.modes.turn.person_game.warp

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.potion.PotionEffectType

class PTVoidWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "void", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.VOID

    override val chance: Double = 20.0

    override fun apply() {
        player!!.game.players.forEach { it.inventory.removeRandom() }
        Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.OPPONENTS).apply(player)

        player.game.turnScheduler.runTaskLater(ScheduleID.BLINDNESS, 3) {
            player.opponents().forEach { it.player.removePotionEffect(PotionEffectType.BLINDNESS) }
        }
    }
}
