package net.hectus.neobb.modes.turn.person_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import com.marcpg.libpg.util.Randomizer
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.BlockTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass

class PTAmethystBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory

class PTBambooButton(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory {
    override val damage: Double = 2.0
}

class PTBarrel(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory

class PTBeeNest(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override val damage: Double = 2.0
    override fun apply() {
        Luck(10).invoke(player!!)
    }
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTHoneyBlock::class, PTAmethystBlock::class)
    }
}

class PTBirchLog(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override fun apply() {
        player!!.nextPlayer().damage(2.0, true)
    }
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTPinkCarpet::class, PTGreenCarpet::class)
    }
}

class PTBlackCarpet(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory {
    override fun counter(source: NeoPlayer, countered: Turn<*>) {
        super.counter(source, countered)
        countered.player!!.damage(countered.damage + 2.0)
    }
}

class PTBlueConcrete(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override val damage: Double
        get() = if (player!!.game.warp is PTIceWarp) 2.0 else 4.0
    override fun apply() {
        Luck(10).invoke(player!!)
    }
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTHoneyBlock::class, PTBlueIce::class)
    }
}

class PTBlueIce(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory

class PTBlueStainedGlass(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), DefensiveCategory {
    override fun apply() {
        player!!.heal(if (player.game.warp is PTIceWarp) 2.0 else 5.0)
    }
    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
    }
    override fun unusable(): Boolean {
        return player!!.game.time == MinecraftTime.MIDNIGHT
    }
}

class PTBrainCoral(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), WinConCategory {
    override val damage: Double = 4.0
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTIronTrapdoor::class)
    }
    override fun unusable(): Boolean {
        return player!!.nextPlayer().health > 4.0
    }
}

class PTBrownStainedGlass(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), DefensiveCategory {
    override fun apply() {
        player!!.heal(if (player.game.warp is PTVillagerWarp) 2.0 else 5.0)
    }
    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
    }
    override fun unusable(): Boolean {
        return player!!.game.time == MinecraftTime.MIDNIGHT
    }
}

class PTCake(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffCategory {
    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(20))
    }
}

class PTCherryButton(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory {
    override val damage: Double = 2.0
}

class PTCherryPressurePlate(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory {
    override fun apply() {
        player!!.addArmor(1.0)
    }
}

class PTDaylightDetector(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory {
    override fun apply() {
        player!!.game.time = MinecraftTime.NOON
    }
}

class PTDiamondBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override val damage: Double
        get() = if (player!!.game.warp is PTVillagerWarp) 2.0 else 4.0
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTLightBlueCarpet::class, PTNoteBlock::class)
    }
}

class PTDripstone(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory {
    override fun item(): ItemStack = ItemStack(Material.POINTED_DRIPSTONE)
}

class PTFenceGate(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory {
    override fun item(): ItemStack = ItemStack(Material.OAK_FENCE_GATE)
    override fun counter(source: NeoPlayer, countered: Turn<*>) {
        super.counter(source, countered)
        countered.player!!.damage(countered.damage + 2.0)
    }
}

class PTFletchingTable(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override val damage: Double = 2.0
    override fun apply() {
        Luck(10).invoke(player!!)
    }
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTDripstone::class, PTAmethystBlock::class)
    }
}

class PTGlowstone(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), UtilityCategory {
    override val damage: Double = 2.0
    override fun apply() {
        ExtraTurn(if (Randomizer.boolByChance(25.0)) 2 else 1).invoke(player!!)
    }
}

class PTGoldBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory

class PTGrayWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override fun apply() {
        player!!.nextPlayer().damage(2.0, true)
    }
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTNoteBlock::class, PTGoldBlock::class)
    }
}

class PTGreenCarpet(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory

class PTHoneyBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory

class PTIronTrapdoor(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory {
    override fun counter(source: NeoPlayer, countered: Turn<*>) {
        super.counter(source, countered)
        countered.player!!.damage(countered.damage + 2.0)
    }
}

class PTLever(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), DefensiveCounterCategory

class PTLightBlueCarpet(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory

class PTNoteBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory

class PTOrangeWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override val damage: Double = 2.0
    override fun apply() {
        Luck(10).invoke(player!!)
    }
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTGreenCarpet::class, PTBarrel::class)
    }
}

class PTPickCarpet(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory

class PTPinkCarpet(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterCategory

class PTPurpleWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), WinConCategory {
    override val damage: Double = 4.0
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTBlackCarpet::class)
    }
    override fun unusable(): Boolean {
        return player!!.nextPlayer().health > 4.0
    }
}

class PTRedStainedGlass(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), DefensiveCategory {
    override fun apply() {
        player!!.heal(if (player.game.warp is PTFireWarp) 2.0 else 5.0)
    }
    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
    }
    override fun unusable(): Boolean {
        return player!!.game.time == MinecraftTime.MIDNIGHT
    }
}

class PTRedWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override val damage: Double
        get() = if (player!!.game.warp is PTFireWarp) 2.0 else 4.0
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTBarrel::class, PTPinkCarpet::class)
    }
}

class PTSeaLantern(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), UtilityCategory {
    override fun apply() {
        player!!.addArmor(1.0)
    }
}

class PTStonecutter(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), DefensiveCounterCategory

class PTVerdantFroglight(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), WinConCategory {
    override val damage: Double = 4.0
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTFenceGate::class)
    }
    override fun unusable(): Boolean {
        return player!!.nextPlayer().health > 4.0
    }
}

class PTWhiteStainedGlass(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), DefensiveCategory {
    override fun apply() {
        player!!.heal(if (player.game.warp is PTSnowWarp) 2.0 else 5.0)
    }
    override fun applyDefense() {
        player!!.addModifier(Modifiers.Player.Default.DEFENDED)
    }
    override fun unusable(): Boolean {
        return player!!.game.time == MinecraftTime.MIDNIGHT
    }
}

class PTWhiteWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackCategory {
    override val damage: Double
        get() = if (player!!.game.warp is PTSnowWarp) 2.0 else 4.0
    override fun counteredBy(): List<KClass<out Turn<*>>> {
        return listOf(PTGoldBlock::class, PTDripstone::class)
    }
}
