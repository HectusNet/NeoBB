package net.hectus.neobb.turn.legacy_game.item;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.turn.default_game.item.ItemTurn;
import net.hectus.neobb.turn.default_game.mob.MobTurn;
import net.hectus.neobb.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LTDinnerboneTag extends ItemTurn implements CounterFunction, NeutralClazz {
    public LTDinnerboneTag(NeoPlayer player) { super(player); }
    public LTDinnerboneTag(ItemStack data, Location location, NeoPlayer player) { super(data, location, player); }

    @Override
    public int cost() {
        return 2;
    }

    @Override
    public ItemStack item() {
        return new ItemBuilder(Material.NAME_TAG).name(Component.text("Dinnerbone")).build();
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(t -> t instanceof MobTurn<?>, "mobs"));
    }
}
