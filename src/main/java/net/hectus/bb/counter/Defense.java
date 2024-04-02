package net.hectus.bb.counter;

import com.marcpg.libpg.storing.Pair;
import net.hectus.bb.PlayerData;

public class Defense extends TurnCounter {
    public Defense(PlayerData player, int turnsLeft) {
        super(player, turnsLeft);
    }

    @Override
    public boolean decrement() {
        boolean result = super.decrement();
        if (result) {
            player.setDefense(Pair.of(false, null));
        }
        return result;
    }
}
