package net.hectus.neobb.game.util

import com.marcpg.libpg.data.time.Time
import net.hectus.neobb.event.TurnEvent
import net.hectus.neobb.modes.lore.DummyItemLoreBuilder
import net.hectus.neobb.modes.lore.ItemLoreBuilder
import net.hectus.neobb.modes.shop.Shop
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnRegistry
import net.hectus.neobb.modes.turn.default_game.StructureTurn
import org.bukkit.block.Block
import org.bukkit.entity.Projectile
import kotlin.reflect.KClass

data class GameInfo(
    val namespace: String,
    val showIntro: Boolean = true,
    val hasStructures: Boolean = true,
    val startingHealth: Double = 20.0,
    val coins: Int = 0,
    val deckSize: Int = 9,
    val totalTime: Time = Time(10, Time.Unit.MINUTES),
    val turnTimer: Int = -1,
    val shop: KClass<out Shop>,
    val loreBuilder: KClass<out ItemLoreBuilder> = DummyItemLoreBuilder::class,
    val turns: List<Turn<*>> = TurnRegistry.turns.filter { it.mode == "any" || it.mode == namespace },
) {
    val customEvents = mutableListOf<Turn<*>>()
    val blockEvents = mutableListOf<Turn<Block>>()
    val flowerEvents = mutableListOf<Turn<Block>>()
    val structureEvents = mutableListOf<StructureTurn>()
    val throwEvents = mutableListOf<Turn<Projectile>>()

    init {
        for (turn in turns) {
            @Suppress("UNCHECKED_CAST")
            when (turn.event) {
                TurnEvent.CUSTOM -> customEvents.add(turn)
                TurnEvent.BLOCK -> blockEvents.add(turn as Turn<Block>)
                TurnEvent.FLOWER -> flowerEvents.add(turn as Turn<Block>)
                TurnEvent.STRUCTURE -> structureEvents.add(turn as StructureTurn)
                TurnEvent.THROW -> throwEvents.add(turn as Turn<Projectile>)
                else -> {}
            }
        }
    }
}
