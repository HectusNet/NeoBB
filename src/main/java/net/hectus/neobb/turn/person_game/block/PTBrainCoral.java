package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.WinConCategory;
import org.bukkit.block.Block;

import java.util.List;

public class PTBrainCoral extends BlockTurn implements WinConCategory {
    public PTBrainCoral(NeoPlayer player) { super(player); }
    public PTBrainCoral(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() {
        return 4;
    }

    @Override
    public List<Class<? extends Turn<?>>> counteredBy() {
        return List.of(PTIronTrapdoor.class);
    }

    @Override
    public boolean unusable() {
        return player.nextPlayer().health() > 4.0;
    }
}
