package net.hectus.neobb.shop;

import net.hectus.neobb.player.NeoPlayer;
import org.jetbrains.annotations.NotNull;

public class DummyShop extends Shop {
    public DummyShop(@NotNull NeoPlayer player) {
        super(player);
    }

    @Override
    public void open() {
        // Do absolutely nothing, because it's just for testing.
    }
}
