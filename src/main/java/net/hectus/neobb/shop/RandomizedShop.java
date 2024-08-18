package net.hectus.neobb.shop;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.player.NeoPlayer;
import org.jetbrains.annotations.NotNull;

public class RandomizedShop extends Shop {
    public RandomizedShop(@NotNull Game game) {
        super(game);
    }

    @Override
    public void open(NeoPlayer player) {
        for (int i = 0; i < game.info().deckSize(); i++) {
            player.inventory.addToDeck(turn(Randomizer.fromCollection(turns), player).item());
        }
        done(player);
    }
}
