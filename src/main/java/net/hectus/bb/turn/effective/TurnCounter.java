package net.hectus.bb.turn.effective;

import net.hectus.bb.player.PlayerData;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class TurnCounter {
    public final PlayerData player;
    protected int turnsLeft;

    public TurnCounter(PlayerData player, int turnsLeft) {
        this.player = player;
        this.turnsLeft = turnsLeft;
    }

    public int turnsLeft() {
        return turnsLeft;
    }

    /**
     * Decrements the turns left by one and depending on the implementation, may do some stuff.
     * @return {@code true}, if the counter is over and the object should be disposed, {@code false} otherwise.
     */
    @OverridingMethodsMustInvokeSuper
    public boolean decrement() {
        return --turnsLeft <= 0;
    }
}
