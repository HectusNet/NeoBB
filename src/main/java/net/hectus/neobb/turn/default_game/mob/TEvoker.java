package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.MobUsage;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Evoker;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TEvoker extends Turn<Evoker> implements MobUsage, CounterbuffFunction, SupernaturalClazz {
    public TEvoker(NeoPlayer player) { super(null, null, player); }
    public TEvoker(Evoker data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.EVOKER_SPAWN_EGG);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public Evoker getValue() {
        return data;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 20), new Buff.ExtraTurn());
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(turn -> turn instanceof TSheep t && t.data().getColor() == DyeColor.BLUE, "blue-sheep"));
    }
}
