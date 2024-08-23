package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import org.bukkit.block.Block;

public class TPackedIce extends BlockTurn implements AttackFunction, ColdClazz {
    public TPackedIce(NeoPlayer player) { super(player); }
    public TPackedIce(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 3;
    }
}
