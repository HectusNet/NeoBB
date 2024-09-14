package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class HTRedstoneLamp extends HereTurn {
    public HTRedstoneLamp(NeoPlayer player) { super(player); }
    public HTRedstoneLamp(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 5; }
}
