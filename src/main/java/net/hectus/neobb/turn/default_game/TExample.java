package net.hectus.neobb.turn.default_game;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import org.bukkit.block.Block;

public class TExample extends BlockTurn {
    public TExample(NeoPlayer player) { super(player); }
    public TExample(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 0;
    }
}
