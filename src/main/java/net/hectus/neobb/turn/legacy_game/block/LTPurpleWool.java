package net.hectus.neobb.turn.legacy_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import org.bukkit.block.Block;

public class LTPurpleWool extends BlockTurn implements AttackFunction, NeutralClazz {
    public LTPurpleWool(NeoPlayer player) { super(player); }
    public LTPurpleWool(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 15;
    }

    @Override
    public boolean unusable() {
        return isDummy() || player.game.history().size() < 10;
    }

    @Override
    public void apply() {
        player.game.win(player);
    }
}
