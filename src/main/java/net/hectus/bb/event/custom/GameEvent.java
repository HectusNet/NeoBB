package net.hectus.bb.event.custom;

import net.hectus.bb.game.Game;
import org.bukkit.event.Event;

public abstract class GameEvent extends Event {
    protected final Game game;

    protected GameEvent(Game game) {
        this.game = game;
    }

    public Game game() {
        return game;
    }
}
