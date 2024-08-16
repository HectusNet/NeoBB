package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import net.hectus.neobb.turn.default_game.CounterFilter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TStonecutter extends Turn<Block> implements BlockUsage, CounterFunction, NeutralClazz {
    public TStonecutter(NeoPlayer player) { super(null, null, player); }
    public TStonecutter(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.STONECUTTER);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public Block getValue() {
        return data;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(turn -> turn.getClass().getSimpleName().endsWith("Wall"), "walls"));
    }

    @Override
    public void counter(@NotNull NeoPlayer source) {
        source.opponents(true).forEach(p -> p.removeModifier("defended"));
    }
}
