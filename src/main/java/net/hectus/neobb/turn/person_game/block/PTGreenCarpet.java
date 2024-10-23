package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.CounterCategory;
import org.bukkit.block.Block;

public class PTGreenCarpet extends BlockTurn implements CounterCategory {
    public PTGreenCarpet(NeoPlayer player) { super(player); }
    public PTGreenCarpet(Block data, NeoPlayer player) { super(data, player); }
}
