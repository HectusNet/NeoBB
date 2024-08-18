package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.TExample;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TCampfire extends Turn<Block> implements BlockUsage, CounterattackFunction, HotClazz {
    public TCampfire(NeoPlayer player) { super(null, null, player); }
    public TCampfire(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public boolean requiresUsageGuide() { return true; }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.CAMPFIRE);
    }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public Block getValue() {
        return data;
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
