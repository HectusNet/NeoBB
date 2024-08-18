package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.MobUsage;
import org.bukkit.Material;
import org.bukkit.entity.PolarBear;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TPolarBear extends Turn<PolarBear> implements MobUsage, BuffFunction, ColdClazz {
    public TPolarBear(NeoPlayer player) { super(null, null, player); }
    public TPolarBear(PolarBear data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.POLAR_BEAR_SPAWN_EGG);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public PolarBear getValue() {
        return data;
    }

    @Override
    public void apply() {
        player.addModifier("no_jump");
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.ALL, PotionEffectType.SPEED, 3));
    }
}
