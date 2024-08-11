package net.hectus.neobb.turn;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.attributes.function.CounterattackFunction;
import net.hectus.neobb.turn.attributes.usage.BlockUsage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TGoldBlock extends Turn<Block> implements BlockUsage, CounterattackFunction, NeutralClazz {
    public TGoldBlock(NeoPlayer player) { super(null, player); }
    public TGoldBlock(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.GOLD_BLOCK);
    }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public List<Class<? extends CounterFilter>> counters() {
        return List.of(NeutralClazz.class, HotClazz.class, NatureClazz.class);
    }

    @Override
    public Block getValue() {
        return data;
    }
}
