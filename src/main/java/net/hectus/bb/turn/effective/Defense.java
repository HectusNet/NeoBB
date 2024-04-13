package net.hectus.bb.turn.effective;

import com.marcpg.libpg.storing.Pair;
import net.hectus.bb.player.PlayerData;

public class Defense extends TurnCounter {
    public final boolean isWall;

    public Defense(PlayerData player, int turnsLeft, boolean isWall) {
        super(player, turnsLeft);
        this.isWall = isWall;
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
