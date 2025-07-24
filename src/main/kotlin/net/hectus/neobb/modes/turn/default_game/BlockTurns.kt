package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import com.marcpg.libpg.util.component
import net.hectus.neobb.buff.*
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Waterlogged
import org.bukkit.entity.Bee
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

abstract class BlockTurn(namespace: String) : Turn<Block>(namespace) {
    override val event: TurnEvent = TurnEvent.BLOCK
}

object TBeeNest : BlockTurn("bee_nest"), AttackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NATURE
    override val cost: Int? = 4

    override fun apply(exec: TurnExec<Block>) {
        if (Randomizer.boolByChance(20.0)) {
            val exec = TurnExec(TBee, exec.player, exec.cord, exec.game.world.spawn(exec.location!!, Bee::class.java))
            exec.turn.buffs.forEach { it(exec.player) }
            exec.turn.apply(exec)
        }
    }
}

object TBlackWool : BlockTurn("black_wool"), CounterattackFunction, BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int? = 5

    override val counters: List<CounterFilter> = listOf(TurnClazz.COLD, TurnClazz.SUPERNATURAL)
    override val buffs: List<Buff<*>> = listOf(Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.ALL))
}

object TBlueBed : BlockTurn("blue_bed"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.SUPERNATURAL
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.COLD, TurnClazz.WATER, TurnClazz.NATURE, TurnClazz.SUPERNATURAL)
    override val buffs: List<Buff<*>> = listOf(Luck(5))
}

object TBlueIce : BlockTurn("blue_ice"), CounterFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.COLD
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.COLD, TurnClazz.WATER, TurnClazz.SUPERNATURAL)
}

object TBrainCoralBlock : BlockTurn("brain_coral_block"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.WATER
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.REDSTONE)
}

object TCampfire : BlockTurn("campfire"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.HOT
    override val cost: Int = 5

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.HOT, TurnClazz.COLD)

    override fun apply(exec: TurnExec<Block>) {
        if (exec.hist.isEmpty()) return
        if (exec.hist.last().turn === TBeeNest) {
            Luck(10).invoke(exec.player)
            ExtraTurn().invoke(exec.player)
        }
    }
}

object TCauldron : BlockTurn("cauldron"), AttackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 2
}

object TComposter : BlockTurn("composter"), AttackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NATURE
    override val cost: Int = 2
}

object TCyanCarpet : BlockTurn("cyan_carpet"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.REDSTONE, TurnClazz.SUPERNATURAL)
    override val buffs: List<Buff<*>> = listOf(ExtraTurn())
}

object TDragonHead : BlockTurn("dragon_head"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.SUPERNATURAL
    override val cost: Int = 6

    override val buffs: List<Buff<*>> = listOf(
        Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.OPPONENTS),
        Effect(PotionEffectType.SLOWNESS, target = Buff.BuffTarget.OPPONENTS),
        Luck(-20, Buff.BuffTarget.OPPONENTS)
    )
}

object TDriedKelpBlock : BlockTurn("dried_kelp_block"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.WATER
    override val cost: Int = 3

    override val buffs: List<Buff<*>> = listOf(Effect(PotionEffectType.JUMP_BOOST, 2))
}

object TFenceGate : BlockTurn("fence_gate"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.REDSTONE
    override val cost: Int = 4

    override val mainItem: ItemStack = ItemStack.of(Material.OAK_FENCE_GATE)

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.HOT)
}

object TFire : BlockTurn("fire"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.HOT
    override val cost: Int = 4

    override val mainItem: ItemStack = ItemStack.of(Material.FLINT_AND_STEEL)

    override val counters: List<CounterFilter> = listOf(TurnClazz.COLD, TurnClazz.REDSTONE, TurnClazz.NATURE)
    override val buffs: List<Buff<*>> = listOf(
        Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.ALL),
        Effect(PotionEffectType.GLOWING, target = Buff.BuffTarget.ALL)
    )
}

object TFireCoral : BlockTurn("fire_coral"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.WATER
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.HOT, TurnClazz.REDSTONE)
}

object TFireCoralFan : BlockTurn("fire_coral_fan"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.WATER
    override val cost: Int = 5

    override val counters: List<CounterFilter> = listOf(TurnClazz.HOT, TurnClazz.REDSTONE)
    override val buffs: List<Buff<*>> = listOf(ChancedBuff(80.0, ExtraTurn()))
}

object TGoldBlock : BlockTurn("gold_block"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 5

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.HOT, TurnClazz.NATURE)
}

object TGreenBed : BlockTurn("green_bed"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.SUPERNATURAL
    override val cost: Int = 2

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.NATURE)
}

object TGreenCarpet : BlockTurn("green_carpet"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 3

    override val counters: List<CounterFilter> = listOf(CounterFilter.of("wool") { it.turn.namespace.endsWith("wool") })
}

object TGreenWool : BlockTurn("green_wool"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NATURE
    override val cost: Int = 5

    override val counters: List<CounterFilter> = listOf(TurnClazz.NATURE, TurnClazz.HOT, TurnClazz.COLD)
}

object THayBlock : BlockTurn("hay_block"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NATURE
    override val cost: Int = 3

    override val counters: List<CounterFilter> = listOf(TurnClazz.COLD, TurnClazz.WATER)
}

object THoneyBlock : BlockTurn("honey_block"), CounterFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NATURE
    override val cost: Int = 5

    override val counters: List<CounterFilter> = listOf(TurnClazz.NATURE, TurnClazz.WATER, TurnClazz.REDSTONE)
}

object THornCoral : BlockTurn("horn_coral"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.WATER
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.NATURE, TurnClazz.NATURE, TurnClazz.WATER)
}

object TIronTrapdoor : BlockTurn("iron_trapdoor"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.WATER, TurnClazz.REDSTONE)
}

object TLava : BlockTurn("lava"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.HOT
    override val maxAmount: Int = 1
    override val cost: Int = 6

    override val mainItem: ItemStack = ItemStack.of(Material.LAVA_BUCKET)

    override fun apply(exec: TurnExec<Block>) {
        exec.player.nextPlayer().player.fireTicks = 6000
        exec.game.turnScheduler.runTaskLater(ScheduleID.BURN, 3) {
            exec.game.eliminate(exec.player.nextPlayer())
        }
    }
}

object TLever : BlockTurn("lever"), CounterFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.REDSTONE
    override val cost: Int = 3

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.REDSTONE)
}

object TLightBlueWool : BlockTurn("light_blue_wool"), AttackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.COLD
    override val cost: Int = 3
}

object TLightningRod : BlockTurn("lightning_rod"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.REDSTONE
    override val cost: Int = 6

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.HOT, TurnClazz.REDSTONE, TurnClazz.SUPERNATURAL)
}

object TMagentaGlazedTerracotta : BlockTurn("magenta_glazed_terracotta"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 4

    override fun unusable(player: NeoPlayer): Boolean {
        return if (player.game.history.isNotEmpty() && player.game.history.last().turn is BlockTurn) {
            // TODO: Find a method of replacing this check!
            false
            // exec.data.getRelative((exec.data.blockData as Directional).facing.oppositeFace) != exec.hist.last().data
        } else true
    }

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.HOT, TurnClazz.COLD, TurnClazz.WATER, TurnClazz.NATURE)
}

object TMagmaBlock : BlockTurn("magma_block"), AttackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.HOT
    override val cost: Int = 4
}

object TMangroveRoots : BlockTurn("mangrove_roots"), CounterFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NATURE
    override val cost: Int = 5

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.WATER, TurnClazz.NATURE)
}

object TNetherrack : BlockTurn("netherrack"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.HOT
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.HOT, TurnClazz.COLD, TurnClazz.WATER)
}

object TOakStairs : BlockTurn("oak_stairs"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NATURE
    override val cost: Int = 5

    override val counters: List<CounterFilter> = listOf(TurnClazz.HOT, TurnClazz.NATURE, TurnClazz.SUPERNATURAL)
    override val buffs: List<Buff<*>> = listOf(ExtraTurn())
}

object TOrangeWool : BlockTurn("orange_wool"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.HOT
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.REDSTONE, TurnClazz.SUPERNATURAL)
}

object TPackedIce : BlockTurn("packed_ice"), AttackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.COLD
    override val cost: Int = 3
}

object TPinkBed : BlockTurn("pink_bed"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.SUPERNATURAL
    override val cost: Int = 4

    override fun apply(exec: TurnExec<Block>) {
        if (Randomizer.boolByChance(70.0)) {
            exec.player.player.clearActivePotionEffects()
        }
    }
}

object TPiston : BlockTurn("piston"), CounterFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.REDSTONE
    override val cost: Int = 6

    override val counters: List<CounterFilter> = listOf(CounterFilter.of("all") { true })
}

object TPowderSnow : BlockTurn("powder_snow"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.COLD
    override val cost: Int = 5
    override val maxAmount: Int = 1

    override val mainItem: ItemStack = ItemStack.of(Material.POWDER_SNOW_BUCKET)

    override fun apply(exec: TurnExec<Block>) {
        exec.player.nextPlayer().player.freezeTicks = 6000
        exec.game.turnScheduler.runTaskLater(ScheduleID.FREEZE, 3) {
            exec.game.eliminate(exec.player.nextPlayer())
        }
    }
}

object TPurpleWool : BlockTurn("purple_wool"), AttackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 4

    override fun unusable(player: NeoPlayer): Boolean {
        val hist = player.game.history
        if (hist.size < 5) return true
        return hist.subList(hist.size - 5, hist.size).any { it.turn is AttackFunction }
    }

    override fun apply(exec: TurnExec<Block>) {
        exec.game.win(exec.player)
    }
}

object TRedBed : BlockTurn("red_bed"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.SUPERNATURAL
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.COLD, TurnClazz.SUPERNATURAL)
}

object TRedCarpet : BlockTurn("red_carpet"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.REDSTONE
    override val cost: Int = 5

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.COLD, TurnClazz.SUPERNATURAL)
    override val buffs: List<Buff<*>> = listOf(ExtraTurn(), ChancedBuff(10.0, Give(TRedBed)))
}

object TRepeater : BlockTurn("repeater"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.REDSTONE
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.COLD, TurnClazz.NATURE)
}

object TRespawnAnchor : BlockTurn("respawn_anchor"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.HOT
    override val cost: Int = 4

    override val buffs: List<Buff<*>> = listOf(
        Teleport({ it.game.history.lastOrNull()?.cord ?: Cord(0.0, 0.0, 0.0) }),
        ChancedBuff(50.0, ExtraTurn())
    )
}

object TSculk : BlockTurn("sculk"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.SUPERNATURAL)
    override val buffs: List<Buff<*>> = listOf(
        Luck(10, Buff.BuffTarget.OPPONENTS),
        Effect(PotionEffectType.DARKNESS, target = Buff.BuffTarget.OPPONENTS)
    )
}

object TSeaLantern : BlockTurn("sea_lantern"), BuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.WATER
    override val cost: Int = 7

    override fun apply(exec: TurnExec<Block>) {
        exec.player.addModifier(Modifiers.Player.REVIVE)
        exec.player.sendMessage(exec.player.locale().component("gameplay.info.revive.start", color = Colors.POSITIVE))
        exec.game.turnScheduler.runTaskLater(ScheduleID.REVIVE, 3) {
            exec.player.removeModifier(Modifiers.Player.REVIVE)
            exec.player.sendMessage(exec.player.locale().component("gameplay.info.revive.end", color = Colors.NEGATIVE))
        }
    }
}

object TSoulSand : BlockTurn("soul_sand"), AttackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.SUPERNATURAL
    override val cost: Int = 3
}

object TSponge : BlockTurn("sponge"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.WATER
    override val cost: Int = 5

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.WATER, TurnClazz.SUPERNATURAL)
    override val buffs: List<Buff<*>> = listOf(ExtraTurn(), Luck(5))

    override fun apply(exec: TurnExec<Block>) {
        if (exec.hist.isEmpty()) return
        if (exec.hist.last().turn === TWater) {
            exec.game.world.setStorm(true)
            Luck(10).invoke(exec.player)
            Effect(PotionEffectType.JUMP_BOOST).invoke(exec.player)
        }
    }
}

object TSpruceLeaves : BlockTurn("spruce_leaves"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.COLD
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.NATURE)
}

object TSpruceTrapdoor : BlockTurn("spruce_trapdoor"), CounterattackFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.COLD
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.WATER, TurnClazz.NATURE)
}

object TStonecutter : BlockTurn("stonecutter"), CounterFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 4

    override val counters: List<CounterFilter> = listOf(CounterFilter.of("walls") { it.turn.namespace.endsWith("wall") })

    override fun counter(source: NeoPlayer, countered: TurnExec<*>) {
        super.counter(source, countered)
        source.opponents().forEach { it.removeModifier(Modifiers.Player.Default.DEFENDED) }
    }
}

object TVerdantFroglight : BlockTurn("verdant_froglight"), CounterFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.NEUTRAL
    override val cost: Int = 2

    override val counters: List<CounterFilter> = listOf(TurnClazz.SUPERNATURAL)
}

object TWater : BlockTurn("water"), CounterFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.WATER
    override val cost: Int = 3

    override val mainItem: ItemStack = ItemStack.of(Material.WATER_BUCKET)

    override val counters: List<CounterFilter> = listOf(CounterFilter.of("waterloggable") { it.turn is BlockTurn && (it.data as Block).blockData is Waterlogged })
}

object TWhiteWool : BlockTurn("white_wool"), CounterbuffFunction {
    override val mode: String = "default"
    override val clazz: TurnClazz? = TurnClazz.COLD
    override val cost: Int = 5

    override val counters: List<CounterFilter> = listOf(TurnClazz.NEUTRAL, TurnClazz.WATER, TurnClazz.REDSTONE, TurnClazz.SUPERNATURAL)
    override val buffs: List<Buff<*>> = listOf(Effect(PotionEffectType.SLOWNESS, 9, Buff.BuffTarget.ALL))
}
