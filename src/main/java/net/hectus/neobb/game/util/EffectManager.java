package net.hectus.neobb.game.util;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.default_game.mob.MobTurn;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Shulker;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

public class EffectManager {
    private LivingEntity highlight;

    public void applyEffects(@NotNull Turn<?> turn) {
        NamedTextColor outlineColor = turn.player().cosmeticOutline();

        if (turn instanceof BlockTurn blockTurn) highlightBlock(blockTurn.data(), outlineColor);
        if (turn instanceof StructureTurn structureTurn) highlightBlock(structureTurn.data().lastBlock(), outlineColor);
        if (turn instanceof MobTurn<?> mobTurn) applyHighlight(mobTurn.data(), outlineColor);

        spawnParticle(turn.location(), turn.player());
    }

    public void spawnParticle(Location loc, @NotNull NeoPlayer player) {
        player.cosmeticPlaceParticle().spawn(loc);
    }

    public void highlightBlock(@NotNull Block block, NamedTextColor outlineColor) {
        if (block.getType().name().contains("GLASS")) return;

        block.getWorld().spawn(block.getLocation(), Shulker.class, shulker -> {
            shulker.setAI(false);
            shulker.setInvisible(true);
            shulker.setInvulnerable(true);
            applyHighlight(shulker, outlineColor);
        });
    }

    public void applyHighlight(@NotNull LivingEntity entity, NamedTextColor outlineColor) {
        clearHighlight();
        highlight = entity;

        entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, -1, 0, false, false));

        Team team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("highlight-" + entity.getUniqueId().getMostSignificantBits());
        team.color(outlineColor);
        team.addEntity(entity);
    }

    public void clearHighlight() {
        if (highlight != null) {
            if (highlight instanceof Shulker) {
                highlight.remove();
            } else {
                highlight.removePotionEffect(PotionEffectType.GLOWING);
            }
        }
    }
}
