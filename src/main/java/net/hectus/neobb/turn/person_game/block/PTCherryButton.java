package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.CounterCategory;
import org.bukkit.block.Block;

public class PTCherryButton extends BlockTurn implements CounterCategory {
    public PTCherryButton(NeoPlayer player) { super(player); }
    public PTCherryButton(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() {
        return 2;
    }
}
