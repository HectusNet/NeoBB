package net.hectus.neobb.turn.default_game.throwable;

import net.hectus.neobb.NeoBB;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.util.ItemBuilder;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.List;

public class TSplashJumpBoostPotion extends ThrowableTurn implements BuffFunction, SupernaturalClazz {
    public TSplashJumpBoostPotion(NeoPlayer player) { super(player); }
    public TSplashJumpBoostPotion(Projectile data, Location impact, NeoPlayer player) { super(data, impact, player); }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public ItemStack item() {
        return new ItemBuilder(Material.SPLASH_POTION)
                .editMeta(meta -> ((PotionMeta) meta).setBasePotionType(PotionType.STRONG_LEAPING))
                .build();
    }

    @Override
    public void apply() {
        if (player.player.hasPotionEffect(PotionEffectType.LEVITATION))
            player.removeModifier(Modifiers.P_EXTRA_TURN);

        player.opponents(true).forEach(p -> {
            if (p.player.hasPotionEffect(PotionEffectType.JUMP_BOOST)) {
                p.player.removePotionEffect(PotionEffectType.JUMP_BOOST);
                p.addModifier(Modifiers.P_NO_JUMP);
                Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, () -> p.removeModifier(Modifiers.P_NO_JUMP), 600);
            }
        });
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }
}
