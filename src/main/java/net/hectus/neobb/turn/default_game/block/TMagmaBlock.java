package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import org.bukkit.block.Block;

public class TMagmaBlock extends BlockTurn implements AttackFunction, HotClazz {
    public TMagmaBlock(NeoPlayer player) { super(player); }
    public TMagmaBlock(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public boolean requiresUsageGuide() { return true; }

    @Override
    public int cost() {
        return 4;
    }
}
