package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.DummyTurn;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import org.bukkit.block.Block;

public class TDirt extends BlockTurn implements DummyTurn {
    public TDirt(NeoPlayer player) { super(player); }
    public TDirt(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 2;
    }
}
