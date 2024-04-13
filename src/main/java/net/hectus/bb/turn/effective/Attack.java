package net.hectus.bb.turn.effective;

import com.marcpg.libpg.storing.Pair;
import net.hectus.bb.player.PlayerData;

public class Attack extends TurnCounter {
    public Attack(PlayerData player) {
        super(player, 1);
    }

    @Override
    public boolean decrement() {
        boolean result = super.decrement();
        if (result) {
            player.game().lose(player);
            player.setAttack(Pair.of(false, null));
        }
        return result;
    }
}
