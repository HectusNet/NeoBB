package net.hectus.bb.turn.effective;

import net.hectus.bb.player.PlayerData;

public class Burning extends TurnCounter {
    public Burning(PlayerData player, int turnsLeft) {
        super(player, turnsLeft);
    }

    @Override
    public boolean decrement() {
        boolean result = super.decrement();
        if (result) {
            player.specialEffects().remove(this);
        }
        return result;
    }
}
