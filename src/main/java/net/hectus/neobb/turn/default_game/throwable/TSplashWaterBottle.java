package net.hectus.neobb.turn.default_game.throwable;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.shop.util.ItemBuilder;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.List;

public class TSplashWaterBottle extends ThrowableTurn implements CounterbuffFunction, WaterClazz {
    public TSplashWaterBottle(NeoPlayer player) { super(player); }
    public TSplashWaterBottle(Projectile data, Location impact, NeoPlayer player) { super(data, impact, player); }

    @Override
    public int cost() {
        return 7;
    }

    @Override
    public ItemStack item() {
        return new ItemBuilder(Material.SPLASH_POTION)
                .editMeta(meta -> ((PotionMeta) meta).setBasePotionType(PotionType.WATER))
                .build();
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 20), new Buff.Effect(Buff.BuffTarget.NEXT, PotionEffectType.SLOWNESS), new Buff.Effect(Buff.BuffTarget.NEXT, PotionEffectType.BLINDNESS));
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(HotClazz.class), CounterFilter.clazz(RedstoneClazz.class));
    }
}
