package net.hectus.neobb.modes.turn.default_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.Randomizer
import com.marcpg.libpg.util.component
import com.marcpg.libpg.util.toCord
import net.hectus.neobb.buff.*
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.attribute.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.material
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.Directional
import org.bukkit.block.data.Waterlogged
import org.bukkit.entity.Bee
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType

abstract class BlockTurn(data: Block?, cord: Cord?, player: NeoPlayer?) : Turn<Block>(data, cord?.plus(Cord(0.5, 0.5, 0.5)), player) {
    override fun item(): ItemStack = ItemStack(this::class.material())
}

class TBeeNest(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction,
    NatureClazz {
    override val cost: Int = 4
    override fun apply() {
        if (Randomizer.boolByChance(20.0)) {
            val bee = TBee(player!!.game.world.spawn(location(), Bee::class.java), location().toCord(), player)
            bee.buffs().forEach { it(player) }
            bee.apply()
        }
    }
}

class TBlackWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    BuffFunction, NeutralClazz {
    override val cost: Int = 5
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.ALL))
    }
}

class TBlueBed(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction,
    SupernaturalClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(
            NatureClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(5))
    }
}

class TBlueIce(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction,
    ColdClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(
            SupernaturalClazz::class))
    }
}

class TBrainCoralBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player),
    CounterattackFunction, WaterClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(RedstoneClazz::class))
    }
}

class TCampfire(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    HotClazz {
    override val cost: Int = 5
    override fun apply() {
        if (player!!.game.history.isEmpty()) return
        if (player.game.history.last() is TBeeNest) {
            Luck(10).invoke(player)
            ExtraTurn().invoke(player)
        }
    }
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(
            ColdClazz::class))
    }
}

class TCauldron(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction,
    NeutralClazz {
    override val cost: Int = 2
}

class TComposter(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction,
    NatureClazz {
    override val cost: Int = 2
}

class TCyanCarpet(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction,
    NeutralClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(RedstoneClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn())
    }
}

class TDragonHead(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction,
    SupernaturalClazz {
    override val cost: Int = 6
    override fun buffs(): List<Buff<*>> {
        return listOf(
            Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.OPPONENTS),
            Effect(PotionEffectType.SLOWNESS, target = Buff.BuffTarget.OPPONENTS),
            Luck(-20, Buff.BuffTarget.OPPONENTS)
        )
    }
}

class TDriedKelpBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction,
    WaterClazz {
    override val cost: Int = 3
    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.JUMP_BOOST, 2))
    }
}

class TFenceGate(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    RedstoneClazz {
    override val cost: Int = 4
    override fun item(): ItemStack = ItemStack(Material.OAK_FENCE_GATE)
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(HotClazz::class))
    }
}

class TFire(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction,
    HotClazz {
    override val cost: Int = 4
    override fun item(): ItemStack = ItemStack(Material.FLINT_AND_STEEL)
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(RedstoneClazz::class), CounterFilter.clazz(
            NatureClazz::class))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.ALL), Effect(PotionEffectType.GLOWING, target = Buff.BuffTarget.ALL))
    }
}

class TFireCoral(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    WaterClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(RedstoneClazz::class))
    }
}

class TFireCoralFan(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction,
    WaterClazz {
    override val cost: Int = 5
    override fun buffs(): List<Buff<*>> {
        return listOf(ChancedBuff(80.0, ExtraTurn()))
    }
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(RedstoneClazz::class))
    }
}

class TGoldBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    NeutralClazz {
    override val cost: Int = 5
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(
            NatureClazz::class))
    }
}

class TGreenBed(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    SupernaturalClazz {
    override val cost: Int = 2
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(NatureClazz::class))
    }
}

class TGreenCarpet(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player),
    CounterattackFunction, NeutralClazz {
    override val cost: Int = 3
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("wool") { it::class.simpleName!!.endsWith("Wool") })
    }
}

class TGreenWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    NatureClazz {
    override val cost: Int = 5
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NatureClazz::class), CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(
            ColdClazz::class))
    }
}

class THayBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    NatureClazz {
    override val cost: Int = 3
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(WaterClazz::class))
    }
}

class THoneyBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction,
    NatureClazz {
    override val cost: Int = 5
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NatureClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(
            RedstoneClazz::class))
    }
}

class THornCoral(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    WaterClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NatureClazz::class), CounterFilter.clazz(NatureClazz::class), CounterFilter.clazz(
            WaterClazz::class))
    }
}

class TIronTrapdoor(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player),
    CounterattackFunction, NeutralClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(
            RedstoneClazz::class))
    }
}

class TLava(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction, HotClazz {
    override val cost: Int = 6
    override val maxAmount: Int = 1
    override fun item(): ItemStack = ItemStack(Material.LAVA_BUCKET)
    override fun apply() {
        player!!.nextPlayer().player.fireTicks = 6000
        player.game.turnScheduler.runTaskLater(ScheduleID.BURN, 3) {
            player.game.eliminate(player.nextPlayer())
        }
    }
    override fun buffs(): List<Buff<*>> = emptyList()
}

class TLever(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction,
    RedstoneClazz {
    override val cost: Int = 3
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(RedstoneClazz::class))
    }
}

class TLightBlueWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction,
    ColdClazz {
    override val cost: Int = 3
}

class TLightningRod(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player),
    CounterattackFunction, RedstoneClazz {
    override val cost: Int = 6
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(
            RedstoneClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }
}

class TMagentaGlazedTerracotta(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player),
    CounterattackFunction, NeutralClazz {
    override val cost: Int = 4
    override fun unusable(): Boolean {
        if (isDummy()) return true
        return if (player!!.game.history.isNotEmpty() && player.game.history.last() is BlockTurn) {
            data!!.getRelative((data.blockData as Directional).facing.oppositeFace) != player.game.history.last().data
        } else true
    }
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(
            ColdClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(NatureClazz::class))
    }
}

class TMagmaBlock(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction,
    HotClazz {
    override val cost: Int = 4
}

class TMangroveRoots(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction,
    NatureClazz {
    override val cost: Int = 5
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(
            NatureClazz::class))
    }
}

class TNetherrack(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    HotClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(
            WaterClazz::class))
    }
}

class TOakStairs(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction,
    NatureClazz {
    override val cost: Int = 5
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(HotClazz::class), CounterFilter.clazz(NatureClazz::class), CounterFilter.clazz(
            SupernaturalClazz::class))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn())
    }
}

class TOrangeWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    HotClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(RedstoneClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }
}

class TPackedIce(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction,
    ColdClazz {
    override val cost: Int = 3
}

class TPinkBed(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction,
    SupernaturalClazz {
    override val cost: Int = 4
    override fun apply() {
        if (Randomizer.boolByChance(70.0)) {
            player!!.player.clearActivePotionEffects()
        }
    }
    override fun buffs(): List<Buff<*>> = emptyList()
}

class TPiston(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction,
    RedstoneClazz {
    override val cost: Int = 6
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("all") { true })
    }
}

class TPowderSnow(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction,
    ColdClazz {
    override val cost: Int = 5
    override val maxAmount: Int = 1
    override fun item(): ItemStack = ItemStack(Material.POWDER_SNOW_BUCKET)
    override fun apply() {
        player!!.nextPlayer().player.freezeTicks = 6000
        player.game.turnScheduler.runTaskLater(ScheduleID.FREEZE, 3) {
            player.game.eliminate(player.nextPlayer())
        }
    }
    override fun buffs(): List<Buff<*>> = emptyList()
}

class TPurpleWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction,
    NeutralClazz {
    override val cost: Int = 4
    override fun unusable(): Boolean {
        if (isDummy()) return true
        val history = player!!.game.history
        if (history.size < 5) return true
        return history.subList(history.size - 5, history.size).any { it is AttackFunction }
    }
    override fun apply() {
        player!!.game.win(player)
    }
}

class TRedBed(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    SupernaturalClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(
            SupernaturalClazz::class))
    }
}

class TRedCarpet(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction,
    RedstoneClazz {
    override val cost: Int = 5
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(
            SupernaturalClazz::class))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn(), ChancedBuff(10.0, Give(TRedBed(null, null, player))))
    }
}

class TRepeater(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterattackFunction,
    RedstoneClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(ColdClazz::class), CounterFilter.clazz(
            NatureClazz::class))
    }
}

class TRespawnAnchor(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction,
    HotClazz {
    override val cost: Int = 4
    override fun buffs(): List<Buff<*>> {
        return listOf(Teleport(player!!.game.history.lastOrNull()?.location()?.toCord() ?: Cord(0.0, 0.0, 0.0)), ChancedBuff(50.0, ExtraTurn()))
    }
}

class TSculk(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction,
    NeutralClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Luck(10, Buff.BuffTarget.OPPONENTS), Effect(PotionEffectType.DARKNESS, target = Buff.BuffTarget.OPPONENTS))
    }
}

class TSeaLantern(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), BuffFunction,
    WaterClazz {
    override val cost: Int = 7
    override fun apply() {
        player!!.addModifier(Modifiers.Player.REVIVE)
        player.sendMessage(player.locale().component("gameplay.info.revive.start", color = Colors.POSITIVE))
        player.game.turnScheduler.runTaskLater(ScheduleID.REVIVE, 3) {
            player.removeModifier(Modifiers.Player.REVIVE)
            player.sendMessage(player.locale().component("gameplay.info.revive.end", color = Colors.NEGATIVE))
        }
    }
    override fun buffs(): List<Buff<*>> = emptyList()
}

class TSoulSand(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), AttackFunction,
    SupernaturalClazz {
    override val cost: Int = 3
}

class TSponge(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction,
    WaterClazz {
    override val cost: Int = 5
    override fun apply() {
        if (player!!.game.history.isEmpty()) return
        if (player.game.history.last() is TWater) {
            player.game.world.setStorm(true)
            Luck(10).invoke(player)
            Effect(PotionEffectType.JUMP_BOOST).invoke(player)
        }
    }
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(
            SupernaturalClazz::class))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(ExtraTurn(), Luck(5))
    }
}

class TSpruceLeaves(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player),
    CounterattackFunction, ColdClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(NatureClazz::class))
    }
}

class TSpruceTrapdoor(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player),
    CounterattackFunction, ColdClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(
            NatureClazz::class))
    }
}

class TStonecutter(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction,
    NeutralClazz {
    override val cost: Int = 4
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("walls") { it::class.simpleName!!.endsWith("Wall") })
    }
    override fun counter(source: NeoPlayer, countered: Turn<*>) {
        super.counter(source, countered)
        source.opponents().forEach { it.removeModifier(Modifiers.Player.Default.DEFENDED) }
    }
}

class TVerdantFroglight(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction,
    NeutralClazz {
    override val cost: Int = 2
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.clazz(SupernaturalClazz::class))
    }
}

class TWater(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterFunction,
    WaterClazz {
    override val cost: Int = 3
    override fun item(): ItemStack = ItemStack(Material.WATER_BUCKET)
    override fun counters(): List<CounterFilter> {
        return listOf(CounterFilter.of("waterloggable") { it is BlockTurn && it.data?.blockData is Waterlogged })
    }
}

class TWhiteWool(data: Block?, cord: Cord?, player: NeoPlayer?) : BlockTurn(data, cord, player), CounterbuffFunction,
    ColdClazz {
    override val cost: Int = 5
    override fun counters(): List<CounterFilter> {
        return listOf(
            CounterFilter.clazz(NeutralClazz::class), CounterFilter.clazz(WaterClazz::class), CounterFilter.clazz(
            RedstoneClazz::class), CounterFilter.clazz(SupernaturalClazz::class))
    }
    override fun buffs(): List<Buff<*>> {
        return listOf(Effect(PotionEffectType.SLOWNESS, 9, Buff.BuffTarget.ALL))
    }
}
