package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.DummyTurn;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import org.bukkit.block.Block;

public class TFlowerPot extends BlockTurn implements DummyTurn {
    public TFlowerPot(NeoPlayer player) { super(player); }
    public TFlowerPot(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 2;
    }
}
