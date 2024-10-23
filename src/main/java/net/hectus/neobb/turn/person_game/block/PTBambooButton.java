package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.CounterCategory;
import org.bukkit.block.Block;

public class PTBambooButton extends BlockTurn implements CounterCategory {
    public PTBambooButton(NeoPlayer player) { super(player); }
    public PTBambooButton(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() {
        return 2;
    }
}
