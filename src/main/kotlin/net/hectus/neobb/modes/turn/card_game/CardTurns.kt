package net.hectus.neobb.modes.turn.card_game

import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.player.NeoPlayer
import org.bukkit.block.Block

abstract class CardTurn(namespace: String) : Turn<Block>(namespace) {
    override val mode: String = "card"
    override val event: TurnEvent = TurnEvent.BLOCK

    // Prevent the suggestion system from doing anything:
    override fun goodChoice(player: NeoPlayer): Boolean = false
}

object CTChest : CardTurn("chest") {
    override val damage: Double = 5.0
}

object CTDaylightDetector : CardTurn("daylight_detector") {
    override val damage: Double = 3.0
}

object CTFlowerPot : CardTurn("flower_pot") {
    override val damage: Double = 2.0
}

object CTJackOLantern : CardTurn("jack_o_lantern") {
    override val damage: Double = 2.0

    override fun apply(exec: TurnExec<Block>) {
        if (exec.player.game.time == MinecraftTime.MIDNIGHT)
            exec.player.damage(3.0)
    }
}

object CTOakTrapdoor : CardTurn("oak_trapdoor") {
    override val damage: Double = 8.0
}

object CTPointedDripstone : CardTurn("pointed_dripstone") {
    override val damage: Double = 6.0
}

object CTRedstoneLamp : CardTurn("redstone_lamp") {
    override val damage: Double = 5.0
}

object CTTorch : CardTurn("torch") {
    override val damage: Double = 6.0
}

object CTWaxedExposedCutCopperStairs : CardTurn("waxed_exposed_cut_copper_stairs") {
    override val damage: Double = 5.0
}
