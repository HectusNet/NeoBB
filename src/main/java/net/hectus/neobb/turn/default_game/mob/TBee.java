package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.MobUsage;
import org.bukkit.Material;
import org.bukkit.entity.Bee;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TBee extends Turn<Bee> implements MobUsage, BuffFunction, NatureClazz {
    public TBee(NeoPlayer player) { super(null, null, player); }
    public TBee(Bee data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.BEE_SPAWN_EGG);
    }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public Bee getValue() {
        return data;
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }
}
