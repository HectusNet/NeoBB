package net.hectus.neobb.shop;

import net.hectus.neobb.player.NeoPlayer;
import org.jetbrains.annotations.NotNull;

public class RandomizedShop extends Shop {
    public RandomizedShop(@NotNull NeoPlayer player) {
        super(player);
    }

    @Override
    public void open() {
        player.inventory.fillInRandomly();
        done();
    }
}
