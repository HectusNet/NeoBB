package net.hectus.neobb.turn.default_game.item;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.ItemUsage;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.TExample;
import net.hectus.neobb.turn.default_game.block.TCauldron;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TIronShovel extends Turn<ItemStack> implements ItemUsage, CounterbuffFunction, NeutralClazz {
    public TIronShovel(NeoPlayer player) { super(null, null, player); }
    public TIronShovel(ItemStack data, Location location, NeoPlayer player) { super(data, location, player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.IRON_SHOVEL);
    }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public ItemStack getValue() {
        return data;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn());
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(turn -> turn instanceof TExample, "dirt"), CounterFilter.of(turn -> turn instanceof TExample, "grass"), CounterFilter.of(turn -> turn instanceof TCauldron, "cauldron"), CounterFilter.clazz(HotClazz.class)); // TODO: Switch to actual turn classes.
    }
}
