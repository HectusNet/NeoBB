package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class HTPointedDripstone extends HereTurn {
    public HTPointedDripstone(NeoPlayer player) { super(player); }
    public HTPointedDripstone(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 6; }
}
