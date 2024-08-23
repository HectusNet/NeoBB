package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TCyanCarpet extends BlockTurn implements CounterbuffFunction, NeutralClazz {
    public TCyanCarpet(NeoPlayer player) { super(player); }
    public TCyanCarpet(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn());
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(RedstoneClazz.class), CounterFilter.clazz(SupernaturalClazz.class));
    }
}
