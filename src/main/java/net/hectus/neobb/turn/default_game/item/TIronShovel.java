package net.hectus.neobb.turn.default_game.item;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import net.hectus.neobb.turn.default_game.block.TCauldron;
import net.hectus.neobb.turn.here_game.HereTurn;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TIronShovel extends ItemTurn implements CounterbuffFunction, NeutralClazz {
    public TIronShovel(NeoPlayer player) { super(player); }
    public TIronShovel(ItemStack data, Location location, NeoPlayer player) { super(data, location, player); }

    @Override
    public boolean requiresUsageGuide() { return true; }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn());
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(turn -> turn instanceof HereTurn, "dirt"), CounterFilter.of(turn -> turn instanceof HereTurn, "grass"), CounterFilter.of(turn -> turn instanceof TCauldron, "cauldron"), CounterFilter.clazz(HotClazz.class)); // TODO: Switch to actual turn classes.
    }
}
