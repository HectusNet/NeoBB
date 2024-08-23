package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TBlueBed extends BlockTurn implements CounterbuffFunction, SupernaturalClazz {
    public TBlueBed(NeoPlayer player) { super(player); }
    public TBlueBed(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 5));
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(ColdClazz.class), CounterFilter.clazz(WaterClazz.class), CounterFilter.clazz(NatureClazz.class), CounterFilter.clazz(SupernaturalClazz.class));
    }
}
