package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.MobUsage;
import org.bukkit.Material;
import org.bukkit.entity.PufferFish;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TPufferfish extends Turn<PufferFish> implements MobUsage, BuffFunction, WaterClazz {
    public TPufferfish(NeoPlayer player) { super(null, null, player); }
    public TPufferfish(PufferFish data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.PUFFERFISH_SPAWN_EGG);
    }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public PufferFish getValue() {
        return data;
    }

    @Override
    public void apply() {
        // TODO: Make player poisoned for 2 turns and kill him afterwards, if not healed.
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }
}
