package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import net.hectus.neobb.turn.default_game.CounterFilter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TCyanCarpet extends Turn<Block> implements BlockUsage, CounterbuffFunction, NeutralClazz {
    public TCyanCarpet(NeoPlayer player) { super(null, null, player); }
    public TCyanCarpet(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.CYAN_CARPET);
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
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn());
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(RedstoneClazz.class), CounterFilter.clazz(SupernaturalClazz.class));
    }
}
