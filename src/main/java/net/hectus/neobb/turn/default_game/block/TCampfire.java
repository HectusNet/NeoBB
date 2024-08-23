package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.TExample;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TCampfire extends BlockTurn implements CounterattackFunction, HotClazz {
    public TCampfire(NeoPlayer player) { super(player); }
    public TCampfire(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public boolean requiresUsageGuide() { return true; }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public void apply() {
        if (player.game.history().isEmpty()) return;
        if (player.game.history().getLast() instanceof TExample) { // TODO: Check if last turn was Bee Nest.
            new Buff.Luck(Buff.BuffTarget.YOU, 10).apply(player);
            new Buff.ExtraTurn().apply(player);
        }
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(HotClazz.class), CounterFilter.clazz(ColdClazz.class));
    }
}
