package net.hectus.bb.event.custom;

import net.hectus.bb.player.PlayerData;
import net.hectus.bb.turn.Warp;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerWarpEvent extends GameEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final PlayerData player;
    private final Warp from;
    private final Warp to;

    public PlayerWarpEvent(@NotNull PlayerData player, Warp from, Warp to) {
        super(player.game());
        this.player = player;
        this.from = from;
        this.to = to;
    }

    public PlayerData player() {
        return player;
    }

    public Warp from() {
        return from;
    }

    public Warp to() {
        return to;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static @NotNull HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
