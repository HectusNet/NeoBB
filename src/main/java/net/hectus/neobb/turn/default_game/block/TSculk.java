package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import net.hectus.neobb.turn.default_game.CounterFilter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TSculk extends Turn<Block> implements BlockUsage, CounterbuffFunction, NeutralClazz {
    public TSculk(NeoPlayer player) { super(null, null, player); }
    public TSculk(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.SCULK);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.OPPONENTS, 10), new Buff.Effect(Buff.BuffTarget.OPPONENTS, PotionEffectType.DARKNESS));
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(SupernaturalClazz.class));
    }

    @Override
    public Block getValue() {
        return data;
    }
}
