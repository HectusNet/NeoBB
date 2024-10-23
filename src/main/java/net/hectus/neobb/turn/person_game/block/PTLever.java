package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.DefensiveCounterCategory;
import org.bukkit.block.Block;

public class PTLever extends BlockTurn implements DefensiveCounterCategory {
    public PTLever(NeoPlayer player) { super(player); }
    public PTLever(Block data, NeoPlayer player) { super(data, player); }
}
