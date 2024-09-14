package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class HTOakTrapdoor extends HereTurn {
    public HTOakTrapdoor(NeoPlayer player) { super(player); }
    public HTOakTrapdoor(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 8; }
}
