package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.buff.*
import net.hectus.neobb.modes.turn.ComboTurn
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoInventory
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.block.Block
import org.bukkit.potion.PotionEffectType

abstract class FlowerTurn(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction,
    NatureClazz {
    override fun goodChoice(player: NeoPlayer): Boolean {
        if (player.game.history.isEmpty()) return false
        val last = player.game.history.last()
        return super.goodChoice(player) && (last is TDirt || last is TFlowerPot)
    }

    override fun buffs(): List<Buff<*>> = emptyList()
}

class TAllium(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 7
    override fun apply() {
        player!!.game.players.forEach { it.inventory.removeRandom() }
    }
}

class TAzureBluet(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 7
    override fun unusable(): Boolean {
        if (isDummy()) return true
        return player!!.game.history.none { it is TAzureBluet }
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(20))
    }
}

class TBlueOrchid(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 6
    override fun apply() {
        player!!.addModifier(Modifiers.Player.Default.ALWAYS_WARP)
    }
}

class TCornflower(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 5
    override fun apply() {
        player!!.game.allowed.add(WaterClazz::class)
        player.game.world.setStorm(true)
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.DARKNESS, target = Buff.BuffTarget.ALL))
    }
}

class TDirt(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), ComboTurn {
    override val cost: Int = 2
}

class TFlowerPot(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), ComboTurn {
    override val cost: Int = 2
}

class TOrangeTulip(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 4
    override fun apply() {
        player!!.game.allowed.add(HotClazz::class)
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn())
    }
}

class TOxeyeDaisy(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 3
    override fun buffs(): List<Buff<*>> {
        return listOf(ChancedBuff(25.0, ExtraTurn()))
    }
}

class TPinkTulip(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 5
    override fun apply() {
        player!!.game.allowed.add(SupernaturalClazz::class)
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn())
    }
}

class TPoppy(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 6
    override fun apply() {
        val players = player!!.game.players
        val temp: NeoInventory = players.first().inventory
        for (i in 0..<players.size - 1) {
            players[i].inventory = players[i + 1].inventory
        }
        players.last().inventory = temp
        players.forEach { it.inventory.switchOwner(it) }
    }
}

class TRedTulip(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 4
    override fun apply() {
        player!!.game.allowed.add(RedstoneClazz::class)
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn())
    }
}

class TSunflower(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 5
    override fun apply() {
        player!!.game.world.clearWeatherDuration = Int.MAX_VALUE
        player.game.world.setStorm(false)
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(10), Effect(PotionEffectType.SPEED, 1, Buff.BuffTarget.ALL))
    }
}

class TWhiteTulip(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 4
    override fun apply() {
        player!!.game.allowed.add(ColdClazz::class)
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn())
    }
}

class TWitherRose(data: Block?, cord: Cord?, player: NeoPlayer?) : FlowerTurn(data, cord, player) {
    override val cost: Int = 5
    override fun apply() {
        player!!.game.allowed.remove(SupernaturalClazz::class)
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(-15, Buff.BuffTarget.NEXT))
    }
}
