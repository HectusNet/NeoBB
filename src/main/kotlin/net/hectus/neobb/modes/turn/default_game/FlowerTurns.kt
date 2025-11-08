package net.hectus.neobb.modes.turn.default_game

import net.hectus.neobb.buff.*
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.attribute.BuffFunction
import net.hectus.neobb.modes.turn.default_game.attribute.TurnClazz
import net.hectus.neobb.player.NeoInventory
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.block.Block
import org.bukkit.potion.PotionEffectType

abstract class FlowerTurn(namespace: String) : BlockTurn(namespace), BuffFunction {
    override val mode: String = "default"
    override val event: TurnEvent = TurnEvent.FLOWER
    override val clazz: TurnClazz? = TurnClazz.NATURE

    override fun goodChoice(player: NeoPlayer): Boolean {
        if (player.game.history.isEmpty()) return false
        val last = player.game.history.last()
        return super.goodChoice(player) && (last.turn === TDirt || last.turn === TFlowerPot)
    }
}

object TAllium : FlowerTurn("allium") {
    override val cost: Int = 7

    override fun apply(exec: TurnExec<Block>) {
        exec.game.players.forEach { it.inventory.removeRandom() }
    }
}

object TAzureBluet : FlowerTurn("azure_bluet") {
    override val cost: Int = 7

    override val buffs: List<Buff<*>> = listOf(Luck(20))

    override fun unusable(player: NeoPlayer): Boolean = player.game.history.none { it.turn === TAzureBluet }
}

object TBlueOrchid : FlowerTurn("blue_orchid") {
    override val cost: Int = 6

    override fun apply(exec: TurnExec<Block>) {
        exec.player.addModifier(Modifiers.Player.Default.ALWAYS_WARP)
    }
}

object TCornflower : FlowerTurn("cornflower") {
    override val cost: Int = 5

    override val buffs: List<Buff<*>> = listOf(Effect(PotionEffectType.DARKNESS, target = Buff.BuffTarget.ALL))

    override fun apply(exec: TurnExec<Block>) {
        exec.game.allowed += TurnClazz.WATER
        exec.game.world.setStorm(true)
    }
}

object TDirt : BlockTurn("dirt") {
    override val mode: String = "default"
    override val clazz: TurnClazz = TurnClazz.NATURE
    override val cost: Int = 1

    override val isCombo: Boolean = true
}

object TFlowerPot : BlockTurn("flower_pot") {
    override val mode: String = "default"
    override val clazz: TurnClazz = TurnClazz.NATURE
    override val cost: Int = 1

    override val isCombo: Boolean = true
}

object TOrangeTulip : FlowerTurn("orange_tulip") {
    override val cost: Int = 4

    override val buffs: List<Buff<*>> = listOf(ExtraTurn())

    override fun apply(exec: TurnExec<Block>) {
        exec.game.allowed += TurnClazz.HOT
    }
}

object TOxeyeDaisy : FlowerTurn("oxeye_daisy") {
    override val cost: Int = 3

    override val buffs: List<Buff<*>> = listOf(ChancedBuff(25.0, ExtraTurn()))
}

object TPinkTulip : FlowerTurn("pink_tulip") {
    override val cost: Int = 5

    override val buffs: List<Buff<*>> = listOf(ExtraTurn())

    override fun apply(exec: TurnExec<Block>) {
        exec.game.allowed += TurnClazz.SUPERNATURAL
    }
}

object TPoppy : FlowerTurn("poppy") {
    override val cost: Int = 6

    override fun apply(exec: TurnExec<Block>) {
        val players = exec.game.players
        val temp: NeoInventory = players.first().inventory
        for (i in 0 until players.size - 1) {
            players[i].inventory = players[i + 1].inventory
        }
        players.last().inventory = temp
        players.forEach { it.inventory.switchOwner(it) }
    }
}

object TRedTulip : FlowerTurn("red_tulip") {
    override val cost: Int = 4

    override val buffs: List<Buff<*>> = listOf(ExtraTurn())

    override fun apply(exec: TurnExec<Block>) {
        exec.game.allowed += TurnClazz.REDSTONE
    }
}

object TSunflower : FlowerTurn("sunflower") {
    override val cost: Int = 5

    override val buffs: List<Buff<*>> = listOf(
        Luck(10),
        Effect(PotionEffectType.SPEED, 1, Buff.BuffTarget.ALL)
    )

    override fun apply(exec: TurnExec<Block>) {
        exec.game.world.clearWeatherDuration = Int.MAX_VALUE
        exec.game.world.setStorm(false)
    }
}

object TWhiteTulip : FlowerTurn("white_tulip") {
    override val cost: Int = 4

    override val buffs: List<Buff<*>> = listOf(ExtraTurn())

    override fun apply(exec: TurnExec<Block>) {
        exec.game.allowed += TurnClazz.COLD
    }
}

object TWitherRose : FlowerTurn("wither_rose") {
    override val cost: Int = 5

    override val buffs: List<Buff<*>> = listOf(Luck(-15, Buff.BuffTarget.NEXT))

    override fun apply(exec: TurnExec<Block>) {
        exec.game.allowed -= TurnClazz.SUPERNATURAL
    }
}
