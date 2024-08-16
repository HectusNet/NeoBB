package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import net.hectus.neobb.turn.default_game.CounterFilter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TBlackWool extends Turn<Block> implements BlockUsage, CounterattackFunction, BuffFunction, NeutralClazz {
    public TBlackWool(NeoPlayer player) { super(null, null, player); }
    public TBlackWool(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.BLACK_WOOL);
    }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(ColdClazz.class), CounterFilter.clazz(SupernaturalClazz.class));
    }

    @Override
    public Block getValue() {
        return data;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.ALL, PotionEffectType.BLINDNESS));
    }
}
