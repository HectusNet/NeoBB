package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.WinConCategory;
import org.bukkit.block.Block;

import java.util.List;

public class PTPurpleWool extends BlockTurn implements WinConCategory {
    public PTPurpleWool(NeoPlayer player) { super(player); }
    public PTPurpleWool(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() {
        return 4;
    }

    @Override
    public List<Class<? extends Turn<?>>> counteredBy() {
        return List.of(PTBlackCarpet.class);
    }

    @Override
    public boolean unusable() {
        return player.nextPlayer().health() > 4.0;
    }
}
