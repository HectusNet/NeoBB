package net.hectus.neobb.event.custom;

import org.bukkit.event.Cancellable;

public class CancellableImpl implements Cancellable {
    public boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
