package net.hectus.neobb.game.util;

import net.hectus.neobb.game.Game;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.usage.BlockUsage;
import net.hectus.neobb.turn.default_game.attributes.usage.MobUsage;
import net.hectus.neobb.turn.default_game.attributes.usage.StructureUsage;
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
    private final Game game;
    private LivingEntity highlight;

    public EffectManager(Game game) {
        this.game = game;
    }

    public void applyEffects(@NotNull Turn<?> turn) {
        NamedTextColor outlineColor = turn.player().cosmeticOutline();

        if (turn instanceof BlockUsage blockUsage) highlightBlock(blockUsage.getValue(), outlineColor);
        if (turn instanceof StructureUsage structureUsage) highlightBlock(structureUsage.getValue().lastBlock(), outlineColor);
        if (turn instanceof MobUsage mobUsage) applyHighlight(mobUsage.getValue(), outlineColor);

        spawnParticle(turn.location(), turn.player());
    }

    public void spawnParticle(Location loc, @NotNull NeoPlayer player) {
        player.cosmeticParticle().spawn(loc);
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
