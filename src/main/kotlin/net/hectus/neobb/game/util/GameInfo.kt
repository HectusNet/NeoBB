package net.hectus.neobb.game.util

import com.marcpg.libpg.data.time.Time
import net.hectus.neobb.modes.lore.DummyItemLoreBuilder
import net.hectus.neobb.modes.lore.ItemLoreBuilder
import net.hectus.neobb.modes.shop.DummyShop
import net.hectus.neobb.modes.shop.Shop
import net.hectus.neobb.modes.turn.Turn
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
    val shop: KClass<out Shop> = DummyShop::class,
    val loreBuilder: KClass<out ItemLoreBuilder> = DummyItemLoreBuilder::class,
    val turns: List<KClass<out Turn<*>>>,
)
