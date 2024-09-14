package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class HTChest extends HereTurn {
    public HTChest(NeoPlayer player) { super(player); }
    public HTChest(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 5; }
}
