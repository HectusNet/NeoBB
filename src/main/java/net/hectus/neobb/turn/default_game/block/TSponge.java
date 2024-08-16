package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.TExample;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TSponge extends Turn<Block> implements BlockUsage, CounterbuffFunction, WaterClazz {
    public TSponge(NeoPlayer player) { super(null, null, player); }
    public TSponge(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.SPONGE);
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
        if (player.game.history().getLast() instanceof TExample) { // TODO: Check if last turn was Water.
            player.game.world().setStorm(true);
            new Buff.Luck(Buff.BuffTarget.YOU, 10).apply(player);
            new Buff.Effect(Buff.BuffTarget.YOU, PotionEffectType.JUMP_BOOST).apply(player);
        }
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn(), new Buff.Luck(Buff.BuffTarget.YOU, 5));
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(NeutralClazz.class), CounterFilter.clazz(WaterClazz.class), CounterFilter.clazz(SupernaturalClazz.class));
    }
}
