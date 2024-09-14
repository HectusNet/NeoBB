package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class HTWaxedExposedCutCopperStairs extends HereTurn {
    public HTWaxedExposedCutCopperStairs(NeoPlayer player) { super(player); }
    public HTWaxedExposedCutCopperStairs(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 5; }
}
