package net.hectus.neobb.shop;

import net.hectus.neobb.game.Game;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Shop {
    protected final Game game;
    protected final List<Class<? extends Turn<?>>> turns;

    protected Shop(@NotNull Game game) {
        this.game = game;
        this.turns = game.info().turns();
    }

    public abstract void open(NeoPlayer player);

    public final void end() {
        game.start();
    }
}
