package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import org.bukkit.block.Block;

public class TCauldron extends BlockTurn implements AttackFunction, NeutralClazz {
    public TCauldron(NeoPlayer player) { super(player); }
    public TCauldron(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 2;
    }
}
