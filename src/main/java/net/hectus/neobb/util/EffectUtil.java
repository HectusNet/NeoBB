package net.hectus.neobb.util;

import net.hectus.neobb.cosmetic.PlaceParticle;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import net.hectus.neobb.turn.default_game.attributes.usage.MobUsage;
import net.hectus.neobb.turn.default_game.attributes.usage.StructureUsage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Shulker;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class EffectUtil {
    private static Shulker BLOCK_HIGHLIGHT;

    public static void applyEffects(Turn<?> turn) {
        if (turn instanceof BlockUsage blockUsage)
            highlightBlock(blockUsage.getValue());

        if (turn instanceof StructureUsage structureUsage)
            highlightBlock(structureUsage.getValue().lastBlock());

        if (turn instanceof MobUsage mobUsage)
            mobUsage.getValue().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, -1, 0, false, false));

        spawnParticle(turn.location(), turn.player().game);
    }

    public static void spawnParticle(Location loc, Game game) {
        PlaceParticle.DEFAULT.spawn(loc, game);
    }

    public static void highlightBlock(@NotNull Block block) {
        if (BLOCK_HIGHLIGHT != null) BLOCK_HIGHLIGHT.remove();
        if (block.getType().name().contains("GLASS")) return;

        BLOCK_HIGHLIGHT = block.getWorld().spawn(block.getLocation(), Shulker.class, shulker -> {
            shulker.setAI(false);
            shulker.setInvisible(true);
            shulker.setInvulnerable(true);
            shulker.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, -1, 0, false, false));
        });
    }
}
