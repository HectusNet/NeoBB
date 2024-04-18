package net.hectus.bb.event.custom;

import net.hectus.bb.player.PlayerData;
import net.hectus.bb.turn.Turn;
import net.hectus.bb.turn.TurnData;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TurnDoneEvent extends GameEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final TurnData turnData;

    public TurnDoneEvent(@NotNull TurnData turnData) {
        super(turnData.player().game());
        this.turnData = turnData;
    }

    public PlayerData player() {
        return turnData.player();
    }

    public TurnData turnData() {
        return turnData;
    }

    public Turn turn() {
        return turnData.turn();
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
