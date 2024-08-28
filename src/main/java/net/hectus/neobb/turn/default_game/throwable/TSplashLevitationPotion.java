package net.hectus.neobb.turn.default_game.throwable;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.util.ItemBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TSplashLevitationPotion extends ThrowableTurn implements BuffFunction, SupernaturalClazz {
    public TSplashLevitationPotion(NeoPlayer player) { super(player); }
    public TSplashLevitationPotion(Projectile data, Location impact, NeoPlayer player) { super(data, impact, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public ItemStack item() {
        return new ItemBuilder(Material.SPLASH_POTION)
                .editMeta(meta -> ((PotionMeta) meta).addCustomEffect(new PotionEffect(PotionEffectType.LEVITATION, 200, 0, true), true))
                .editMeta(meta -> ((PotionMeta) meta).setColor(Color.WHITE))
                .build();
    }

    @Override
    public void apply() {
        if (player.player.hasPotionEffect(PotionEffectType.LEVITATION))
            player.removeModifier("extra-turn");

        player.game.turnScheduler.runTaskLater("levitation", () -> player.opponents(true).forEach(p -> {
            if (p.player.hasPotionEffect(PotionEffectType.LEVITATION))
                player.game.eliminatePlayer(p);
        }), 3);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn());
    }
}
