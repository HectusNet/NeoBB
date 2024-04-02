package net.hectus.bb.counter;

import com.marcpg.libpg.storing.Pair;
import net.hectus.bb.PlayerData;

public class Attack extends TurnCounter {
    public Attack(PlayerData player, int turnsLeft) {
        super(player, turnsLeft);
    }

    public Attack(PlayerData player) {
        super(player, 1);
    }

    @Override
    public boolean decrement() {
        boolean result = super.decrement();
        if (result) {
            player.lose();
            player.setAttack(Pair.of(false, null));
        }
        return result;
    }
}
