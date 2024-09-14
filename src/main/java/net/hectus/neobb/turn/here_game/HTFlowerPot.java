package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class HTFlowerPot extends HereTurn {
    public HTFlowerPot(NeoPlayer player) { super(player); }
    public HTFlowerPot(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 2; }
}
