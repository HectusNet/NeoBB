package net.hectus.neobb.turn.legacy_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import org.bukkit.block.Block;

import java.util.List;

public class LTSeaPickle extends BlockTurn implements AttackFunction, BuffFunction, NatureClazz {
    public LTSeaPickle(NeoPlayer player) { super(player); }
    public LTSeaPickle(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 0;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn(Buff.BuffTarget.NEXT, 2));
    }
}
