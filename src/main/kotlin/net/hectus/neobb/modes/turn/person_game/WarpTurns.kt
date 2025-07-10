package net.hectus.neobb.modes.turn.person_game

import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.MinecraftTime
import net.hectus.neobb.buff.Buff
import net.hectus.neobb.buff.Effect
import net.hectus.neobb.game.util.ScheduleID
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StaticStructure
import net.hectus.neobb.matrix.structure.StaticStructures
import net.hectus.neobb.modes.turn.default_game.WarpTurn
import net.hectus.neobb.modes.turn.default_game.attribute.Clazz
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Modifiers
import org.bukkit.entity.Snowman
import org.bukkit.potion.PotionEffectType
import kotlin.reflect.KClass

abstract class PWarpTurn(data: PlacedStructure?, cord: Cord, name: String, player: NeoPlayer?) : WarpTurn(data, cord, "person-$name", player), WarpCategory {
    override val cost: Int = 0
    override val allows: List<KClass<out Clazz>> = listOf(Clazz::class)
    override val temperature: Temperature = Temperature.NORMAL
}

class PTAmethystWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "amethyst", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.AMETHYST
    override val damage: Double = 1.0
    override val chance: Double = 10.0
    override fun apply() {
        player!!.health = player.game.info.startingHealth
    }
}

class PTFireWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "fire", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.FIRE
    override val chance: Double = 50.0
    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.Person.FIRE_ENTITIES)
        player.game.turnScheduler.runTaskTimer(ScheduleID.BURN, 1) {
            player.game.players.forEach { it.damage(1.0) }
        }
        player.game.time = MinecraftTime.NOON
    }
}

class PTIceWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "ice", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.ICE
    override val chance: Double = 50.0
    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.Person.BLUE_ENTITIES)
        player.game.turnScheduler.runTaskTimer(ScheduleID.ICE, 1) {
            player.game.players.forEach { it.damage(1.0) }
        }
        player.game.time = MinecraftTime.NOON
    }
}

class PTSnowWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "snow", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.SNOW
    override val chance: Double = 40.0
    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.Person.WHITE_ENTITIES)
        player.game.world.spawn(player.location(), Snowman::class.java)
        player.game.addModifier(Modifiers.Game.Person.SNOW_GOLEM)
        player.game.turnScheduler.runTaskTimer(ScheduleID.SNOW_GOLEM, 1, { player.game.hasModifier(Modifiers.Game.Person.SNOW_GOLEM) }) {
            player.opponents().forEach { it.damage(1.0) }
        }
    }
}

class PTVillagerWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "villager", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.VILLAGER
    override val chance: Double = 40.0
    override fun apply() {
        player!!.game.addModifier(Modifiers.Game.Person.VILLAGER_ENTITIES)
        player.inventory.addRandom()
    }
}

class PTVoidWarp(data: PlacedStructure?, cord: Cord, player: NeoPlayer?) : PWarpTurn(data, cord, "void", player) {
    override val staticStructure: StaticStructure = StaticStructures.Person.Warp.VOID
    override val chance: Double = 20.0
    override fun apply() {
        player!!.game.players.forEach { it.inventory.removeRandom() }
        Effect(PotionEffectType.BLINDNESS, target = Buff.BuffTarget.OPPONENTS).invoke(player)
        player.game.turnScheduler.runTaskLater(ScheduleID.BLINDNESS, 3) {
            player.opponents().forEach { it.player.removePotionEffect(PotionEffectType.BLINDNESS) }
        }
    }
}
