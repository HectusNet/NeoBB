package net.hectus.neobb.shop;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import org.jetbrains.annotations.NotNull;

public class RandomizedShop extends Shop {
    public RandomizedShop(@NotNull Game game) {
        super(game);
    }

    @Override
    public void open(NeoPlayer player) {
        for (int i = 0; i < game.info().deckSize(); i++) {
            Turn<?> turn = turn(Randomizer.fromCollection(turns), player);
            player.inventory.addToDeck(turn.item(), turn);
        }
        done(player);
    }
}
