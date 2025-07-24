package net.hectus.neobb.modes.turn.person_game

import com.marcpg.libpg.display.location
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.default_game.WarpTurn
import net.hectus.neobb.modes.turn.default_game.attribute.TurnClazz
import net.hectus.neobb.util.Modifiers
import org.bukkit.entity.Snowman
import org.bukkit.potion.PotionEffectType

abstract class PWarpTurn(namespace: String) : WarpTurn(namespace), WarpCategory {
    override val mode: String = "person"

    override val allows: List<TurnClazz> = TurnClazz.entries
    override val temperature: Temperature = Temperature.NORMAL
}

object PTAmethystWarp : PWarpTurn("person_amethyst_warp") {
    override val damage: Double = 1.0
    override val chance: Double = 10.0

    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.AMETHYST

    override fun apply(exec: TurnExec<PlacedStructure>) {
        exec.player.health = exec.game.info.startingHealth
    }
}

object PTFireWarp : PWarpTurn("person_fire_warp") {
    override val chance: Double = 50.0

    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.FIRE

    override fun apply(exec: TurnExec<PlacedStructure>) {
        exec.game.addModifier(Modifiers.Game.Person.FIRE_ENTITIES)
        exec.game.turnScheduler.runTaskTimer(ScheduleID.BURN, 1) {
            exec.game.players.forEach { it.damage(1.0) }
        }
        exec.game.time = MinecraftTime.NOON
    }
}

object PTIceWarp : PWarpTurn("person_ice_warp") {
    override val chance: Double = 50.0

    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.ICE

    override fun apply(exec: TurnExec<PlacedStructure>) {
        exec.game.addModifier(Modifiers.Game.Person.BLUE_ENTITIES)
        exec.game.turnScheduler.runTaskTimer(ScheduleID.ICE, 1) {
            exec.game.players.forEach { it.damage(1.0) }
        }
        exec.game.time = MinecraftTime.NOON
    }
}

object PTSnowWarp : PWarpTurn("person_snow_warp") {
    override val chance: Double = 40.0

    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.SNOW

    override fun apply(exec: TurnExec<PlacedStructure>) {
        exec.game.addModifier(Modifiers.Game.Person.WHITE_ENTITIES)
        exec.game.world.spawn(exec.player.location(), Snowman::class.java)
        exec.game.addModifier(Modifiers.Game.Person.SNOW_GOLEM)
        exec.game.turnScheduler.runTaskTimer(ScheduleID.SNOW_GOLEM, 1, { exec.game.hasModifier(Modifiers.Game.Person.SNOW_GOLEM) }) {
            exec.player.opponents().forEach { it.damage(1.0) }
        }
    }
}

object PTVillagerWarp : PWarpTurn("person_villager_warp") {
    override val chance: Double = 40.0

    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.VILLAGER

    override fun apply(exec: TurnExec<PlacedStructure>) {
        exec.game.addModifier(Modifiers.Game.Person.VILLAGER_ENTITIES)
        exec.player.inventory.addRandom()
    }
}

object PTVoidWarp : PWarpTurn("person_void_warp") {
    override val chance: Double = 20.0

    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.VOID

    override val buffs: List<Buff<*>> = listOf(Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.OPPONENTS))

    override fun apply(exec: TurnExec<PlacedStructure>) {
        exec.game.players.forEach { it.inventory.removeRandom() }
        exec.game.turnScheduler.runTaskLater(ScheduleID.BLINDNESS, 3) {
            exec.player.opponents().forEach { it.player.removePotionEffect(PotionEffectType.BLINDNESS) }
        }
    }
}
