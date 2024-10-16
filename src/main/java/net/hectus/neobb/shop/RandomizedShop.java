package net.hectus.neobb.shop;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import org.jetbrains.annotations.NotNull;

public class RandomizedShop extends Shop {
    public RandomizedShop(@NotNull NeoPlayer player) {
        super(player);
    }

    @Override
    public void open() {
        for (int i = 0; i < player.game.info().deckSize(); i++) {
            Turn<?> turn = turn(Randomizer.fromCollection(turns), player);
            turn.items().forEach(item -> player.inventory.addToDeck(item, turn));
        }
        done();
    }
}
