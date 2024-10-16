package net.hectus.neobb.game.util;

import com.marcpg.libpg.data.time.Time;
import net.hectus.neobb.lore.ItemLoreBuilder;
import net.hectus.neobb.shop.Shop;
import net.hectus.neobb.turn.Turn;

import java.util.List;

public record GameInfo(boolean hasStructures, int coins, int deckSize, Time totalTime, int turnTimer, Class<? extends Shop> shop, Class<? extends ItemLoreBuilder> loreBuilder, List<Class<? extends Turn<?>>> turns) {}
