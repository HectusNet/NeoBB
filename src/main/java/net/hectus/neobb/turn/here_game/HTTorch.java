package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class HTTorch extends HereTurn {
    public HTTorch(NeoPlayer player) { super(player); }
    public HTTorch(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 6; }
}
