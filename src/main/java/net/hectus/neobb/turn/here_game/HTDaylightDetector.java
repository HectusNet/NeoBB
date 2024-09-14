package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class HTDaylightDetector extends HereTurn {
    public HTDaylightDetector(NeoPlayer player) { super(player); }
    public HTDaylightDetector(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 3; }
}
