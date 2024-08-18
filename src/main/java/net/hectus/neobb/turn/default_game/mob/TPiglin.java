package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.function.EventFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.MobUsage;
import org.bukkit.Material;
import org.bukkit.entity.Piglin;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TPiglin extends Turn<Piglin> implements MobUsage, EventFunction, HotClazz {
    public TPiglin(NeoPlayer player) { super(null, null, player); }
    public TPiglin(Piglin data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public boolean requiresUsageGuide() { return true; }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.PIGLIN_SPAWN_EGG);
    }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public Piglin getValue() {
        return data;
    }

    @Override
    public void triggerEvent() {
        data.addScoreboardTag(player.game.id);
    }

    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.YOU, PotionEffectType.BLINDNESS), new Buff.Effect(Buff.BuffTarget.YOU, PotionEffectType.SLOWNESS), new Buff.Luck(Buff.BuffTarget.YOU, 30));
    }
}
