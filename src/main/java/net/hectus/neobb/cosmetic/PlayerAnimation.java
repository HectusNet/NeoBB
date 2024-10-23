package net.hectus.neobb.cosmetic;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.util.Cord;
import net.hectus.neobb.util.Utilities;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

public enum PlayerAnimation {
    NONE((p, c) -> {}),
    LIGHTNING((p, c) -> p.getWorld().strikeLightningEffect(p.getLocation())),
    ANVIL((p, c) -> p.getLocation().clone().add(0, 5, 0).getBlock().setType(Material.ANVIL, true)),
    EXPLODE((p, c) -> p.getLocation().createExplosion(p, 0.0f, false, false)),
    FIREWORK((p, c) -> p.getWorld().spawn(p.getLocation(), Firework.class, f -> {
        f.setTicksToDetonate(0);
        FireworkMeta meta = f.getFireworkMeta();
        meta.addEffect(FireworkEffect.builder()
                .withColor(Color.fromRGB(ThreadLocalRandom.current().nextInt(0x000000, 0xFFFFFF)))
                .build());
        f.setFireworkMeta(meta);
    })),
    FROST((p, c) -> {}), // TODO: Cool frost effect going around map!
    MOB_RAIN((p, c) -> {
        AtomicInteger mobsLeft = new AtomicInteger(10);
        Bukkit.getScheduler().runTaskTimer(NeoBB.PLUGIN, r -> {
            if (mobsLeft.decrementAndGet() <= 0) r.cancel();

            Location loc = c.add(new Cord(ThreadLocalRandom.current().nextDouble(9), 5, ThreadLocalRandom.current().nextDouble(9))).toLocation(p.getWorld());
            loc.getWorld().spawnEntity(loc, Randomizer.fromCollection(Utilities.ENTITY_TYPES), true);
        }, 0, 20);
    });

    public final BiConsumer<Player, Cord> action;

    PlayerAnimation(BiConsumer<Player, Cord> action) {
        this.action = action;
    }

    public void play(@NotNull NeoPlayer player) {
        action.accept(player.player, player.game.warp().lowCorner());
    }
}
