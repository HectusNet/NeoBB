package net.hectus.bb.event;

import com.marcpg.libpg.lang.Translation;
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent;
import net.hectus.bb.game.Game;
import net.hectus.bb.game.GameManager;
import net.hectus.bb.game.util.ImproperTurnException;
import net.hectus.bb.game.util.TurnUnusableException;
import net.hectus.bb.player.PlayerData;
import net.hectus.bb.shop.ShopItemUtilities;
import net.hectus.bb.turn.Turn;
import net.hectus.bb.turn.TurnData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class GameEvents implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockFromTo(@NotNull BlockFromToEvent event) { // Cancel fluid flow
        if (event.getBlock().getType() != Material.DRAGON_EGG)
            event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockExplode(@NotNull BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        Game game = GameManager.getGame(event.getPlayer());
        if (game != null) {
            game.ticker().resetTurnCountdown();
            game.placementHandler.add(event.getBlock(), event.getPlayer());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
            Material material = event.getMaterial();
            Player player = event.getPlayer();

            if (material.name().endsWith("_SPAWN_EGG")) {
                Location loc = Objects.requireNonNull(event.getInteractionPoint());
                switch (material) {
                    case BLAZE_SPAWN_EGG -> turn(player, Turn.BLAZE, null, loc.getWorld().spawn(loc, Blaze.class));
                    case PIGLIN_SPAWN_EGG -> turn(player, Turn.PIGLIN, null, loc.getWorld().spawn(loc, Piglin.class));
                    case POLAR_BEAR_SPAWN_EGG -> turn(player, Turn.POLAR_BEAR, null, loc.getWorld().spawn(loc, PolarBear.class));
                    case AXOLOTL_SPAWN_EGG -> turn(player, Turn.AXOLOTL, null, loc.getWorld().spawn(loc, Axolotl.class));
                    case BEE_SPAWN_EGG -> turn(player, Turn.BEE, null, loc.getWorld().spawn(loc, Bee.class));
                    case SHEEP_SPAWN_EGG -> turn(player, Turn.SHEEP, null, loc.getWorld().spawn(loc, Sheep.class));
                    case PHANTOM_SPAWN_EGG -> turn(player, Turn.PHANTOM, null, loc.getWorld().spawn(loc, Phantom.class));
                    case EVOKER_SPAWN_EGG -> turn(player, Turn.EVOKER, null, loc.getWorld().spawn(loc, Evoker.class));
                    default -> { return; }
                }
                event.setCancelled(true);
            } else if (material == Material.IRON_SHOVEL) {
                turn(player, Turn.IRON_SHOVEL, null, null);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerTeleport(@NotNull PlayerTeleportEvent event) {
        if (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            turn(event.getPlayer(), Turn.ENDER_PEARL, null, null);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityPlace(@NotNull EntityPlaceEvent event) {
        if (event.getEntity() instanceof Boat)
            turn(event.getPlayer(), Turn.BOAT, null, event.getEntity());
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleEnter(@NotNull VehicleEnterEvent event) {
        if (event.getEntered() instanceof Player player && event.getVehicle() instanceof Boat) {
            PlayerData playerData = GameManager.getPlayerData(player);
            if (playerData != null) {
                playerData.modifiers.disable("boat_damage");
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(@NotNull ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player player) {
            if (event.getEntity() instanceof ThrownPotion potion) {
                if (potion.getPotionMeta().getBasePotionType() == PotionType.WATER) {
                    turn(player, Turn.SPLASH_WATER_BOTTLE, null, null);
                } else if (potion.getEffects().size() == 1) {
                    if (potion.getEffects().iterator().next().getType() == PotionEffectType.JUMP) {
                        turn(player, Turn.SPLASH_JUMP_BOOST_POTION, null, null);
                    } else if (potion.getEffects().iterator().next().getType() == PotionEffectType.LEVITATION) {
                        turn(player, Turn.SPLASH_LEVITATION_POTION, null, null);
                    }
                }
            } else if (event.getEntity() instanceof Snowball && event.getHitEntity() instanceof Player) {
                turn(player, Turn.SNOWBALL, null, null);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(@NotNull PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.CHORUS_FRUIT) {
            turn(event.getPlayer(), Turn.CHORUS_FRUIT, null, null);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFlowerPotManipulate(PlayerFlowerPotManipulateEvent event) {
        for (Turn turn : Turn.values()) {
            if (turn.type == Turn.ItemType.BLOCK && turn.name().startsWith("FLOWER_") && turn.items.contains(event.getItem().getType())) {
                try {
                    event.setCancelled(turn(event.getPlayer(), ShopItemUtilities.getTurn(event.getItem().getType()), event.getFlowerpot(), null));
                    return;
                } catch (IllegalArgumentException ignored) {}
            }
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTransform(@NotNull EntityTransformEvent event) {
        if (event.getTransformReason() == EntityTransformEvent.TransformReason.PIGLIN_ZOMBIFIED) {
            Game game = GameManager.getFromScoreboardTags(event.getEntity().getScoreboardTags());
            if (game == null) return;
            Objects.requireNonNull(Turn.PIGLIN.buffs).forEach(buff -> buff.apply(game.turning()));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTargetLivingEntity(@NotNull EntityTargetLivingEntityEvent event) {
        event.setCancelled(true); // Cancel any major AI stuff, except basic movement, i hope XD
    }

    /**
     * Does the turn-related stuff.
     * @param player The player who did this turn.
     * @param turn The turn enum of the turn.
     * @param block Optionally, a block related to this turn.
     * @return {@code true} if the turn is not valid and should be cancelled or undone. <br>
     *         {@code false} otherwise.
     */
    public static boolean turn(Player player, Turn turn, @Nullable Block block, @Nullable Entity entity) {
        PlayerData playerData = GameManager.getPlayerData(player);
        if (playerData == null) {
            player.sendActionBar(Translation.component(player.locale(), "command.giveup.not_in_match"));
        } else if (!playerData.game().hasStarted()) {
            return true;
        } else {
            try {
                playerData.game().turn(new TurnData(playerData, turn, block, entity));
            } catch (TurnUnusableException | ImproperTurnException e) {
                player.sendActionBar(Component.text(e.getMessage(), NamedTextColor.YELLOW));
                return true;
            }
        }
        return false;
    }
}
