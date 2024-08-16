package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import net.hectus.neobb.turn.default_game.CounterFilter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TGreenCarpet extends Turn<Block> implements BlockUsage, CounterattackFunction, NeutralClazz {
    public TGreenCarpet(NeoPlayer player) { super(null, null, player); }
    public TGreenCarpet(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.GREEN_CARPET);
    }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public Block getValue() {
        return data;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(turn -> turn.getClass().getSimpleName().endsWith("Wool"), "wool"));
    }
}
