package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TGreenBed extends BlockTurn implements CounterattackFunction, SupernaturalClazz {
    public TGreenBed(NeoPlayer player) { super(player); }
    public TGreenBed(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(NatureClazz.class));
    }
}
