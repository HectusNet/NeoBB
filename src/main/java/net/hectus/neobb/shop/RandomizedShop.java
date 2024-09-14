package net.hectus.neobb.shop;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.lore.ItemLoreBuilder;
import org.jetbrains.annotations.NotNull;

public class RandomizedShop extends Shop {
    public RandomizedShop(@NotNull Game game, ItemLoreBuilder loreBuilder) {
        super(game, loreBuilder);
    }

    @Override
    public void open(NeoPlayer player) {
        for (int i = 0; i < game.info().deckSize(); i++) {
            Turn<?> turn = turn(Randomizer.fromCollection(turns), player);
            turn.items().forEach(item -> player.inventory.addToDeck(item, turn));
        }
        done(player);
    }
}
