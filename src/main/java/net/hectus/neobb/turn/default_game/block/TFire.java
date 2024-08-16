package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import net.hectus.neobb.turn.default_game.CounterFilter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TFire extends Turn<Block> implements BlockUsage, CounterbuffFunction, HotClazz {
    public TFire(NeoPlayer player) { super(null, null, player); }
    public TFire(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.FLINT_AND_STEEL);
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
        return List.of(new Buff.Effect(Buff.BuffTarget.ALL, PotionEffectType.BLINDNESS), new Buff.Effect(Buff.BuffTarget.ALL, PotionEffectType.GLOWING));
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(ColdClazz.class), CounterFilter.clazz(RedstoneClazz.class), CounterFilter.clazz(NatureClazz.class));
    }
}
