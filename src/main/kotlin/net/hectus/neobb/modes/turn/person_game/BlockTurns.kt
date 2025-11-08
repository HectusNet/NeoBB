package net.hectus.neobb.modes.turn.person_game

import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.ExtraTurn
import net.hectus.neobb.buff.Luck
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.BlockTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.luckChance
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack

object PTAmethystBlock : BlockTurn("amethyst_block"), CounterCategory {
    override val mode: String = "person"
}

object PTBambooButton : BlockTurn("bamboo_button"), CounterCategory {
    override val mode: String = "person"
    override val damage: Double = 2.0
}

object PTBarrel : BlockTurn("barrel"), CounterCategory {
    override val mode: String = "person"
}

object PTBeeNest : BlockTurn("bee_nest"), AttackCategory {
    override val mode: String = "person"
    override val damage: Double = 2.0

    override val buffs: List<Buff<*>> = listOf(Luck(10))

    override fun counteredBy(): List<Turn<*>> = listOf(PTHoneyBlock, PTAmethystBlock)
}

object PTBirchLog : BlockTurn("birch_log"), AttackCategory {
    override val mode: String = "person"

    override fun counteredBy(): List<Turn<*>> = listOf(PTPinkCarpet, PTGreenCarpet)

    override fun apply(exec: TurnExec<Block>) {
        exec.player.targetPlayer().damage(2.0, true)
    }
}

object PTBlackCarpet : BlockTurn("black_carpet"), CounterCategory {
    override val mode: String = "person"

    override fun counter(source: NeoPlayer, countered: TurnExec<*>) {
        super.counter(source, countered)
        countered.player.damage((countered.turn.damage ?: 0.0) + 2.0)
    }
}

object PTBlueConcrete : BlockTurn("blue_concrete"), AttackCategory {
    override val mode: String = "person"
    override val damage: Double = 2.0

    override val buffs: List<Buff<*>> = listOf(Luck(10))

    override fun counteredBy(): List<Turn<*>> = listOf(PTHoneyBlock, PTBlueIce)

    override fun apply(exec: TurnExec<Block>) {
        if (exec.game.warp is PTIceWarp)
            exec.player.targetPlayer().damage(2.0)
    }
}

object PTBlueIce : BlockTurn("blue_ice"), CounterCategory {
    override val mode: String = "person"
}

object PTBlueStainedGlass : BlockTurn("blue_stained_glass"), DefensiveCategory {
    override val mode: String = "person"

    override fun unusable(player: NeoPlayer): Boolean = player.game.time == MinecraftTime.MIDNIGHT

    override fun applyDefense(exec: TurnExec<*>) {
        exec.player.addModifier(Modifiers.Player.Default.DEFENDED)
    }

    override fun apply(exec: TurnExec<Block>) {
        exec.player.heal(if (exec.game.warp is PTIceWarp) 2.0 else 5.0)
    }
}

object PTBrainCoral : BlockTurn("brain_coral"), WinConCategory {
    override val mode: String = "person"
    override val damage: Double = 4.0

    override fun counteredBy(): List<Turn<*>> = listOf(PTIronTrapdoor)

    override fun unusable(player: NeoPlayer): Boolean = player.targetPlayer().health > 4.0
}

object PTBrownStainedGlass : BlockTurn("brown_stained_glass"), DefensiveCategory {
    override val mode: String = "person"

    override fun unusable(player: NeoPlayer): Boolean = player.game.time == MinecraftTime.MIDNIGHT

    override fun applyDefense(exec: TurnExec<*>) {
        exec.player.addModifier(Modifiers.Player.Default.DEFENDED)
    }

    override fun apply(exec: TurnExec<Block>) {
        exec.player.heal(if (exec.game.warp is PTVillagerWarp) 2.0 else 5.0)
    }
}

object PTCake : BlockTurn("cake"), BuffCategory {
    override val mode: String = "person"

    override val buffs: List<Buff<*>> = listOf(Luck(20))
}

object PTCherryButton : BlockTurn("cherry_button"), CounterCategory {
    override val mode: String = "person"
    override val damage: Double = 2.0
}

object PTCherryPressurePlate : BlockTurn("cherry_pressure_plate"), CounterCategory {
    override val mode: String = "person"

    override fun apply(exec: TurnExec<Block>) {
        exec.player.addArmor(1.0)
    }
}

object PTDaylightDetector : BlockTurn("daylight_detector"), CounterCategory {
    override val mode: String = "person"

    override fun apply(exec: TurnExec<Block>) {
        exec.game.time = MinecraftTime.NOON
    }
}

object PTDiamondBlock : BlockTurn("diamond_block"), AttackCategory {
    override val mode: String = "person"
    override val damage: Double = 2.0

    override fun counteredBy(): List<Turn<*>> = listOf(PTLightBlueCarpet, PTNoteBlock)

    override fun apply(exec: TurnExec<Block>) {
        if (exec.game.warp is PTVillagerWarp)
            exec.player.targetPlayer().damage(2.0)
    }
}

object PTDripstone : BlockTurn("dripstone"), CounterCategory {
    override val mode: String = "person"

    override val mainItem: ItemStack = ItemStack.of(Material.POINTED_DRIPSTONE)
}

object PTFenceGate : BlockTurn("fence_gate"), CounterCategory {
    override val mode: String = "person"

    override val mainItem: ItemStack = ItemStack.of(Material.OAK_FENCE_GATE)

    override fun counter(source: NeoPlayer, countered: TurnExec<*>) {
        super.counter(source, countered)
        countered.player.damage((countered.turn.damage ?: 0.0) + 2.0)
    }
}

object PTFletchingTable : BlockTurn("fletching_table"), AttackCategory {
    override val mode: String = "person"
    override val damage: Double = 2.0

    override val buffs: List<Buff<*>> = listOf(Luck(10))

    override fun counteredBy(): List<Turn<*>> = listOf(PTDripstone, PTAmethystBlock)
}

object PTGlowstone : BlockTurn("glowstone"), UtilityCategory {
    override val mode: String = "person"
    override val damage: Double = 2.0

    override fun apply(exec: TurnExec<Block>) {
        ExtraTurn(if (0.25.luckChance(exec.player.luck)) 2 else 1).invoke(exec.player)
    }
}

object PTGoldBlock : BlockTurn("gold_block"), CounterCategory {
    override val mode: String = "person"
}

object PTGrayWool : BlockTurn("gray_wool"), AttackCategory {
    override val mode: String = "person"

    override fun counteredBy(): List<Turn<*>> = listOf(PTNoteBlock, PTGoldBlock)

    override fun apply(exec: TurnExec<Block>) {
        exec.player.targetPlayer().damage(2.0, true)
    }
}

object PTGreenCarpet : BlockTurn("green_carpet"), CounterCategory {
    override val mode: String = "person"
}

object PTHoneyBlock : BlockTurn("honey_block"), CounterCategory {
    override val mode: String = "person"
}

object PTIronTrapdoor : BlockTurn("iron_trapdoor"), CounterCategory {
    override val mode: String = "person"

    override fun counter(source: NeoPlayer, countered: TurnExec<*>) {
        super.counter(source, countered)
        countered.player.damage((countered.turn.damage ?: 0.0) + 2.0)
    }
}

object PTLever : BlockTurn("lever"), DefensiveCounterCategory {
    override val mode: String = "person"
}

object PTLightBlueCarpet : BlockTurn("light_blue_carpet"), CounterCategory {
    override val mode: String = "person"
}

object PTNoteBlock : BlockTurn("note_block"), CounterCategory {
    override val mode: String = "person"
}

object PTOrangeWool : BlockTurn("orange_wool"), AttackCategory {
    override val mode: String = "person"
    override val damage: Double = 2.0

    override val buffs: List<Buff<*>> = listOf(Luck(10))

    override fun counteredBy(): List<Turn<*>> = listOf(PTGreenCarpet, PTBarrel)
}

object PTPinkCarpet : BlockTurn("pink_carpet"), CounterCategory {
    override val mode: String = "person"
}

object PTPurpleWool : BlockTurn("purple_wool"), WinConCategory {
    override val mode: String = "person"
    override val damage: Double = 4.0

    override fun counteredBy(): List<Turn<*>> = listOf(PTBlackCarpet)

    override fun unusable(player: NeoPlayer): Boolean = player.targetPlayer().health > 4.0
}

object PTRedStainedGlass : BlockTurn("red_stained_glass"), DefensiveCategory {
    override val mode: String = "person"

    override fun unusable(player: NeoPlayer): Boolean = player.game.time == MinecraftTime.MIDNIGHT

    override fun applyDefense(exec: TurnExec<*>) {
        exec.player.addModifier(Modifiers.Player.Default.DEFENDED)
    }

    override fun apply(exec: TurnExec<Block>) {
        exec.player.heal(if (exec.game.warp is PTFireWarp) 2.0 else 5.0)
    }
}

object PTRedWool : BlockTurn("red_wool"), AttackCategory {
    override val mode: String = "person"
    override val damage: Double = 2.0

    override fun counteredBy(): List<Turn<*>> = listOf(PTBarrel, PTPinkCarpet)

    override fun apply(exec: TurnExec<Block>) {
        if (exec.game.warp is PTFireWarp)
            exec.player.targetPlayer().damage(2.0)
    }
}

object PTSeaLantern : BlockTurn("sea_lantern"), UtilityCategory {
    override val mode: String = "person"

    override fun apply(exec: TurnExec<Block>) {
        exec.player.addArmor(1.0)
    }
}

object PTStonecutter : BlockTurn("stonecutter"), DefensiveCounterCategory {
    override val mode: String = "person"
}

object PTVerdantFroglight : BlockTurn("verdant_froglight"), WinConCategory {
    override val mode: String = "person"
    override val damage: Double = 4.0

    override fun counteredBy(): List<Turn<*>> = listOf(PTFenceGate)

    override fun unusable(player: NeoPlayer): Boolean = player.targetPlayer().health > 4.0
}

object PTWhiteStainedGlass : BlockTurn("white_stained_glass"), DefensiveCategory {
    override val mode: String = "person"

    override fun unusable(player: NeoPlayer): Boolean  = player.game.time == MinecraftTime.MIDNIGHT

    override fun applyDefense(exec: TurnExec<*>) {
        exec.player.addModifier(Modifiers.Player.Default.DEFENDED)
    }

    override fun apply(exec: TurnExec<Block>) {
        exec.player.heal(if (exec.game.warp is PTSnowWarp) 2.0 else 5.0)
    }
}

object PTWhiteWool : BlockTurn("white_wool"), AttackCategory {
    override val mode: String = "person"
    override val damage: Double = 2.0

    override fun counteredBy(): List<Turn<*>> = listOf(PTGoldBlock, PTDripstone)

    override fun apply(exec: TurnExec<Block>) {
        if (exec.game.warp is PTSnowWarp)
            exec.player.targetPlayer().damage(2.0)
    }
}
