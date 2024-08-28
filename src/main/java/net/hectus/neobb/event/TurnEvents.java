package net.hectus.neobb.event;

import net.hectus.neobb.NeoBB;
import net.hectus.neobb.event.custom.CancellableImpl;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.block.*;
import net.hectus.neobb.turn.default_game.item.TChorusFruit;
import net.hectus.neobb.turn.default_game.item.TIronShovel;
import net.hectus.neobb.turn.default_game.mob.*;
import net.hectus.neobb.turn.default_game.other.TBoat;
import net.hectus.neobb.turn.default_game.throwable.*;
import net.hectus.neobb.turn.default_game.warp.*;
import net.hectus.neobb.util.Colors;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("DataFlowIssue")
public class TurnEvents implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.BLACK_STAINED_GLASS_PANE)
            event.setCancelled(true);

        NeoPlayer player = GameManager.player(event.getPlayer(), true);
        if (unusable(event.getPlayer(), player, event, "You cannot place blocks right now!")) return;
        player.game.resetTurnCountdown();

        Block block = event.getBlock();

        if (StructureManager.isStructureMaterial(block.getType())) {
            if (player.game.arena.currentPlacedBlocksAmount() == 0) {
                Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, () -> {
                    if (player.game.arena.currentPlacedBlocksAmount() > 1) {
                        handleStructure(player, block, event);
                    } else {
                        blockTurn(event, block, player);
                    }
                }, 30);
            } else {
                handleStructure(player, block, event);
            }
        } else {
            blockTurn(event, block, player);
        }
    }

    private void blockTurn(BlockPlaceEvent event, @NotNull Block block, NeoPlayer player) {
        switch (block.getType()) {
            case BEE_NEST -> player.game.turn(new TBeeNest(block, player), event);
            case BLACK_WOOL -> player.game.turn(new TBlackWool(block, player), event);
            case BLUE_BED -> player.game.turn(new TBlueBed(block, player), event);
            case BLUE_ICE -> player.game.turn(new TBlueIce(block, player), event);
            case BRAIN_CORAL_BLOCK -> player.game.turn(new TBrainCoralBlock(block, player), event);
            case CAMPFIRE -> player.game.turn(new TCampfire(block, player), event);
            case CAULDRON -> player.game.turn(new TCauldron(block, player), event);
            case COMPOSTER -> player.game.turn(new TComposter(block, player), event);
            case CYAN_CARPET -> player.game.turn(new TCyanCarpet(block, player), event);
            case DRAGON_HEAD -> player.game.turn(new TDragonHead(block, player), event);
            case DRIED_KELP_BLOCK -> player.game.turn(new TDriedKelpBlock(block, player), event);
            case FIRE -> player.game.turn(new TFire(block, player), event);
            case FIRE_CORAL -> player.game.turn(new TFireCoral(block, player), event);
            case FIRE_CORAL_FAN -> player.game.turn(new TFireCoralFan(block, player), event);
            case GOLD_BLOCK -> player.game.turn(new TGoldBlock(block, player), event);
            case GREEN_BED -> player.game.turn(new TGreenBed(block, player), event);
            case GREEN_CARPET -> player.game.turn(new TGreenCarpet(block, player), event);
            case GREEN_WOOL -> player.game.turn(new TGreenWool(block, player), event);
            case HAY_BLOCK -> player.game.turn(new THayBlock(block, player), event);
            case HONEY_BLOCK -> player.game.turn(new THoneyBlock(block, player), event);
            case HORN_CORAL -> player.game.turn(new THornCoral(block, player), event);
            case IRON_TRAPDOOR -> player.game.turn(new TIronTrapdoor(block, player), event);
            case LAVA -> player.game.turn(new TLava(block, player), event);
            case LEVER -> player.game.turn(new TLever(block, player), event);
            case LIGHTNING_ROD -> player.game.turn(new TLightningRod(block, player), event);
            case LIGHT_BLUE_WOOL -> player.game.turn(new TLightBlueWool(block, player), event);
            case MAGENTA_GLAZED_TERRACOTTA -> player.game.turn(new TMagentaGlazedTerracotta(block, player), event);
            case MAGMA_BLOCK -> player.game.turn(new TMagmaBlock(block, player), event);
            case MANGROVE_ROOTS -> player.game.turn(new TMangroveRoots(block, player), event);
            case NETHERRACK -> player.game.turn(new TNetherrack(block, player), event);
            case OAK_FENCE_GATE -> player.game.turn(new TFenceGate(block, player), event);
            case OAK_STAIRS -> player.game.turn(new TOakStairs(block, player), event);
            case ORANGE_WOOL -> player.game.turn(new TOrangeWool(block, player), event);
            case PACKED_ICE -> player.game.turn(new TPackedIce(block, player), event);
            case PINK_BED -> player.game.turn(new TPinkBed(block, player), event);
            case POWDER_SNOW -> player.game.turn(new TPowderSnow(block, player), event);
            case PURPLE_WOOL -> player.game.turn(new TPurpleWool(block, player), event);
            case RED_BED -> player.game.turn(new TRedBed(block, player), event);
            case RED_CARPET -> player.game.turn(new TRedCarpet(block, player), event);
            case REPEATER -> player.game.turn(new TRepeater(block, player), event);
            case RESPAWN_ANCHOR -> player.game.turn(new TRespawnAnchor(block, player), event);
            case SCULK -> player.game.turn(new TSculk(block, player), event);
            case SEA_LANTERN -> player.game.turn(new TSeaLantern(block, player), event);
            case SOUL_SAND -> player.game.turn(new TSoulSand(block, player), event);
            case SPONGE -> player.game.turn(new TSponge(block, player), event);
            case SPRUCE_LEAVES -> player.game.turn(new TSpruceLeaves(block, player), event);
            case SPRUCE_TRAPDOOR -> player.game.turn(new TSpruceTrapdoor(block, player), event);
            case STONECUTTER -> player.game.turn(new TStonecutter(block, player), event);
            case VERDANT_FROGLIGHT -> player.game.turn(new TVerdantFroglight(block, player), event);
            case WATER -> player.game.turn(new TWater(block, player), event);
            case WHITE_WOOL -> player.game.turn(new TWhiteWool(block, player), event);
            default -> event.setCancelled(true);
        }
    }

    private void handleStructure(@NotNull NeoPlayer player, Block block, BlockPlaceEvent event) {
        Structure structure = StructureManager.match(player.game.arena);
        if (structure == null) return;
        switch (structure.name) {
            case "amethyst-warp" -> player.game.turn(new TAmethystWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "cliff-warp" -> player.game.turn(new TCliffWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "desert-warp" -> player.game.turn(new TDesertWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "end-warp" -> player.game.turn(new TEndWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "frozen-warp" -> player.game.turn(new TFrozenWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "meadow-warp" -> player.game.turn(new TMeadowWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "mushroom-warp" -> player.game.turn(new TMushroomWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "nerd-warp" -> player.game.turn(new TNerdWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "nether-warp" -> player.game.turn(new TNetherWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "ocean-warp" -> player.game.turn(new TOceanWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "redstone-warp" -> player.game.turn(new TRedstoneWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "sun-warp" -> player.game.turn(new TSunWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "void-warp" -> player.game.turn(new TVoidWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
            case "wood-warp" -> player.game.turn(new TWoodWarp(new PlacedStructure(structure, block), block.getWorld(), player), event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        if (event.getAction() == Action.LEFT_CLICK_BLOCK && event.getMaterial().name().endsWith("_SPAWN_EGG")) {
            NeoPlayer player = GameManager.player(event.getPlayer(), true);
            if (unusable(event.getPlayer(), player, event, "You cannot spawn mobs right now!")) return;

            Location loc = Objects.requireNonNull(event.getInteractionPoint());
            switch (event.getMaterial()) {
                case AXOLOTL_SPAWN_EGG -> player.game.turn(new TAxolotl(loc.getWorld().spawn(loc, Axolotl.class), player), event);
                case BEE_SPAWN_EGG -> player.game.turn(new TBee(loc.getWorld().spawn(loc, Bee.class), player), event);
                case BLAZE_SPAWN_EGG -> player.game.turn(new TBlaze(loc.getWorld().spawn(loc, Blaze.class), player), event);
                case EVOKER_SPAWN_EGG -> player.game.turn(new TEvoker(loc.getWorld().spawn(loc, Evoker.class), player), event);
                case PHANTOM_SPAWN_EGG -> player.game.turn(new TPhantom(loc.getWorld().spawn(loc, Phantom.class), player), event);
                case PIGLIN_SPAWN_EGG -> player.game.turn(new TPiglin(loc.getWorld().spawn(loc, Piglin.class), player), event);
                case POLAR_BEAR_SPAWN_EGG -> player.game.turn(new TPolarBear(loc.getWorld().spawn(loc, PolarBear.class), player), event);
                case PUFFERFISH_SPAWN_EGG -> player.game.turn(new TPufferfish(loc.getWorld().spawn(loc, PufferFish.class), player), event);
                case SHEEP_SPAWN_EGG -> player.game.turn(new TSheep(loc.getWorld().spawn(loc, Sheep.class), player), event);
            }
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityPlace(@NotNull EntityPlaceEvent event) {
        if (event.getEntity() instanceof Boat boat) {
            NeoPlayer player = GameManager.player(event.getPlayer(), true);
            if (unusable(event.getPlayer(), player, event, "You cannot place boats right now!")) return;

            player.game.turn(new TBoat(boat, player), event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(@NotNull PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.CHORUS_FRUIT) {
            NeoPlayer player = GameManager.player(event.getPlayer(), true);
            if (unusable(event.getPlayer(), player, event, "You cannot consume chorus fruits right now!")) return;

            player.game.turn(new TChorusFruit(event.getItem(), event.getPlayer().getLocation(), player), event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTransform(@NotNull EntityTransformEvent event) {
        if (event.getTransformReason() == EntityTransformEvent.TransformReason.PIGLIN_ZOMBIFIED) {
            Game game = GameManager.game(event.getEntity().getScoreboardTags());
            if (game == null) return;
            NeoPlayer player = game.currentPlayer();
            new TPiglin(game.currentPlayer()).buffs().forEach(b -> b.apply(player));
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionSplash(@NotNull PotionSplashEvent event) {
        if (event.getPotion().getShooter() instanceof Player p) {
            NeoPlayer player = GameManager.player(p, true);
            if (unusable(p, player, new CancellableImpl(), "You cannot use potions right now!")) return;

            PotionMeta meta = event.getPotion().getPotionMeta();
            if (meta.hasBasePotionType()) {
                switch (meta.getBasePotionType()) {
                    case WATER -> player.game.turn(new TSplashWaterBottle(event.getPotion(), event.getPotion().getLocation(), player), event);
                    case STRONG_LEAPING -> player.game.turn(new TSplashJumpBoostPotion(event.getPotion(), event.getPotion().getLocation(), player), event);
                }
            } else if (meta.hasCustomEffect(PotionEffectType.LEVITATION)) {
                player.game.turn(new TSplashLevitationPotion(event.getPotion(), event.getPotion().getLocation(), player), event);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(@NotNull ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player p) {
            NeoPlayer player = GameManager.player(p, true);
            if (unusable(p, player, new CancellableImpl(), "You cannot throw things right now!")) return;

            if (event.getEntity() instanceof Snowball snowball) {
                player.game.turn(new TSnowball(snowball, snowball.getLocation(), player), event);
            } else if (event.getEntity() instanceof EnderPearl enderPearl) {
                player.game.turn(new TEnderPearl(enderPearl, enderPearl.getLocation(), player), event);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
        NeoPlayer player = GameManager.player(event.getPlayer(), true);
        if (unusable(event.getPlayer(), player, event, "You cannot drop items right now!")) return;

        if (event.getItemDrop().getItemStack().getType() == Material.IRON_SHOVEL) {
            player.game.turn(new TIronShovel(event.getItemDrop().getItemStack(), event.getItemDrop().getLocation(), player), event);
        }
    }

    private boolean unusable(Player fallbackPlayer, NeoPlayer player, Cancellable event, String message) {
        if (player == null) {
            if (fallbackPlayer.getGameMode() != GameMode.CREATIVE) // Allow builders and admins to do this.
                event.setCancelled(true);
            return true;
        }

        if (!player.game.started() || player.game.currentPlayer() != player) {
            player.sendMessage(Component.text(message, Colors.NEGATIVE));
            event.setCancelled(true);
            return true;
        }
        return false;
    }
}
