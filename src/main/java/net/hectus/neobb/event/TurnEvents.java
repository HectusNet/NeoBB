package net.hectus.neobb.event;

import com.marcpg.libpg.util.Randomizer;
import io.papermc.paper.event.player.PlayerNameEntityEvent;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.event.custom.CancellableImpl;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.game.HectusGame;
import net.hectus.neobb.game.mode.CardGame;
import net.hectus.neobb.game.mode.DefaultGame;
import net.hectus.neobb.game.mode.LegacyGame;
import net.hectus.neobb.game.mode.PersonGame;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.card_game.*;
import net.hectus.neobb.turn.default_game.block.*;
import net.hectus.neobb.turn.default_game.item.TChorusFruit;
import net.hectus.neobb.turn.default_game.item.TIronShovel;
import net.hectus.neobb.turn.default_game.mob.*;
import net.hectus.neobb.turn.default_game.other.TBoat;
import net.hectus.neobb.turn.default_game.other.TNoteBlock;
import net.hectus.neobb.turn.default_game.structure.*;
import net.hectus.neobb.turn.default_game.structure.glass_wall.*;
import net.hectus.neobb.turn.default_game.throwable.*;
import net.hectus.neobb.turn.default_game.warp.*;
import net.hectus.neobb.turn.legacy_game.block.LTPurpleWool;
import net.hectus.neobb.turn.legacy_game.block.LTSeaPickle;
import net.hectus.neobb.turn.legacy_game.block.LTTnt;
import net.hectus.neobb.turn.legacy_game.item.LTDinnerboneTag;
import net.hectus.neobb.turn.legacy_game.item.LTEnchantedGoldenApple;
import net.hectus.neobb.turn.legacy_game.item.LTLightningTrident;
import net.hectus.neobb.turn.legacy_game.other.LTNoteBlock;
import net.hectus.neobb.turn.legacy_game.structure.LTDaylightSensorStrip;
import net.hectus.neobb.turn.legacy_game.structure.LTNetherPortal;
import net.hectus.neobb.turn.legacy_game.structure.LTPumpkinWall;
import net.hectus.neobb.turn.legacy_game.structure.LTRedstoneBlockWall;
import net.hectus.neobb.turn.legacy_game.structure.glass_wall.LTLightBlueGlassWall;
import net.hectus.neobb.turn.legacy_game.warp.*;
import net.hectus.neobb.turn.person_game.block.*;
import net.hectus.neobb.turn.person_game.item.PTSuspiciousStew;
import net.hectus.neobb.turn.person_game.other.PTArmorStand;
import net.hectus.neobb.turn.person_game.other.PTPainting;
import net.hectus.neobb.turn.person_game.structure.*;
import net.hectus.neobb.turn.person_game.throwable.PTSnowball;
import net.hectus.neobb.turn.person_game.throwable.PTSplashPotion;
import net.hectus.neobb.turn.person_game.warp.*;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Modifiers;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.block.data.type.SeaPickle;
import org.bukkit.entity.*;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.TNTPrimeEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntityTransformEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("DataFlowIssue")
public class TurnEvents implements Listener {
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockPlace(@NotNull BlockPlaceEvent event) {
        if (event.getBlock().getType() == Material.BLACK_STAINED_GLASS_PANE)
            event.setCancelled(true);

        NeoPlayer player = GameManager.player(event.getPlayer(), true);
        if (unusable(event.getPlayer(), player, true, event, "You cannot place blocks right now!")) return;

        Block block = event.getBlock();

        player.game.resetTurnCountdown();
        if (player.game.info().hasStructures() && StructureManager.isStructureMaterial(block.getType())) {
            if (player.game.arena.currentPlacedBlocksAmount() == 0) {
                Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, () -> {
                    if (player.game.arena.currentPlacedBlocksAmount() > 1) {
                        handleStructure(event, block, player);
                    } else {
                        blockTurn(event, block, player);
                    }
                }, 30);
            } else {
                handleStructure(event, block, player);
            }
        } else {
            blockTurn(event, block, player);
        }
    }

    private void blockTurn(BlockPlaceEvent event, Block block, @NotNull NeoPlayer player) {
        switch (player.game) {
            case CardGame ignored -> {
                switch (block.getType()) {
                    case CHEST -> player.game.turn(new CTChest(block, player), event);
                    case DAYLIGHT_DETECTOR -> player.game.turn(new CTDaylightDetector(block, player), event);
                    case FLOWER_POT -> player.game.turn(new CTFlowerPot(block, player), event);
                    case JACK_O_LANTERN -> player.game.turn(new CTJackOLantern(block, player), event);
                    case OAK_DOOR -> player.game.turn(new CTOakDoor(block, player), event);
                    case OAK_FENCE_GATE -> player.game.turn(new CTOakFenceGate(block, player), event);
                    case OAK_TRAPDOOR -> player.game.turn(new CTOakTrapdoor(block, player), event);
                    case POINTED_DRIPSTONE -> player.game.turn(new CTPointedDripstone(block, player), event);
                    case REDSTONE_LAMP -> player.game.turn(new CTRedstoneLamp(block, player), event);
                    case TORCH -> player.game.turn(new CTTorch(block, player), event);
                    case WAXED_EXPOSED_CUT_COPPER_STAIRS -> player.game.turn(new CTWaxedExposedCutCopperStairs(block, player), event);
                    default -> event.setCancelled(true);
                }
            }
            case HectusGame ignored -> {
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
                    case PURPLE_WOOL -> player.game.turn(player.game instanceof LegacyGame ? new LTPurpleWool(block, player) : new TPurpleWool(block, player), event);
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
                    case DIRT, FLOWER_POT -> {} // Do nothing, but don't cancel!
                    default -> event.setCancelled(true);
                }
            }
            case PersonGame ignored -> {
                switch (block.getType()) {
                    case LIGHT_BLUE_CARPET -> player.game.turn(new PTLightBlueCarpet(block, player), event);
                    case DAYLIGHT_DETECTOR -> player.game.turn(new PTDaylightDetector(block, player), event);
                    case RED_WOOL -> player.game.turn(new PTRedWool(block, player), event);
                    case BEE_NEST -> player.game.turn(new PTBeeNest(block, player), event);
                    case CHERRY_PRESSURE_PLATE -> player.game.turn(new PTCherryPressurePlate(block, player), event);
                    case LEVER -> player.game.turn(new PTLever(block, player), event);
                    case DIAMOND_BLOCK -> player.game.turn(new PTDiamondBlock(block, player), event);
                    case PINK_CARPET -> player.game.turn(new PTPinkCarpet(block, player), event);
                    case BIRCH_LOG -> player.game.turn(new PTBirchLog(block, player), event);
                    case CAKE -> player.game.turn(new PTCake(block, player), event);
                    case BAMBOO_BUTTON -> player.game.turn(new PTBambooButton(block, player), event);
                    case ORANGE_WOOL -> player.game.turn(new PTOrangeWool(block, player), event);
                    case BRAIN_CORAL -> player.game.turn(new PTBrainCoral(block, player), event);
                    case GLOWSTONE -> player.game.turn(new PTGlowstone(block, player), event);
                    case RED_STAINED_GLASS -> player.game.turn(new PTRedStainedGlass(block, player), event);
                    case CHERRY_BUTTON -> player.game.turn(new PTCherryButton(block, player), event);
                    case BLUE_CONCRETE -> player.game.turn(new PTBlueConcrete(block, player), event);
                    case GREEN_CARPET -> player.game.turn(new PTGreenCarpet(block, player), event);
                    case IRON_TRAPDOOR -> player.game.turn(new PTIronTrapdoor(block, player), event);
                    case BARREL -> player.game.turn(new PTBarrel(block, player), event);
                    case GRAY_WOOL -> player.game.turn(new PTGrayWool(block, player), event);
                    case BROWN_STAINED_GLASS -> player.game.turn(new PTBrownStainedGlass(block, player), event);
                    case GOLD_BLOCK -> player.game.turn(new PTGoldBlock(block, player), event);
                    case VERDANT_FROGLIGHT -> player.game.turn(new PTVerdantFroglight(block, player), event);
                    case FLETCHING_TABLE -> player.game.turn(new PTFletchingTable(block, player), event);
                    case HONEY_BLOCK -> player.game.turn(new PTHoneyBlock(block, player), event);
                    case WHITE_STAINED_GLASS -> player.game.turn(new PTWhiteStainedGlass(block, player), event);
                    case BLUE_ICE -> player.game.turn(new PTBlueIce(block, player), event);
                    case BLUE_STAINED_GLASS -> player.game.turn(new PTBlueStainedGlass(block, player), event);
                    case POINTED_DRIPSTONE -> player.game.turn(new PTDripstone(block, player), event);
                    case NOTE_BLOCK -> player.game.turn(new PTNoteBlock(block, player), event);
                    case BLACK_CARPET -> player.game.turn(new PTBlackCarpet(block, player), event);
                    case OAK_FENCE_GATE -> player.game.turn(new PTFenceGate(block, player), event);
                    case AMETHYST_BLOCK -> player.game.turn(new PTAmethystBlock(block, player), event);
                    case WHITE_WOOL -> player.game.turn(new PTWhiteWool(block, player), event);
                    case STONECUTTER -> player.game.turn(new PTStonecutter(block, player), event);
                    case SEA_LANTERN -> player.game.turn(new PTSeaLantern(block, player), event);
                    case PURPLE_WOOL -> player.game.turn(new PTPurpleWool(block, player), event);
                    default -> event.setCancelled(true);
                }
            }
            default -> event.setCancelled(true);
        }
    }

    private void handleStructure(BlockPlaceEvent event, @NotNull Block block, @NotNull NeoPlayer player) {
        Structure structure = StructureManager.match(player.game.arena);
        if (structure == null) return;

        PlacedStructure s = new PlacedStructure(structure, block);
        World w = block.getWorld();
        if (player.game instanceof HectusGame) {
            switch (structure.name) {
                case "iron_bar_jail" -> player.game.turn(new TIronBarJail(s, player), event);
                case "oak_door_turtling" -> player.game.turn(new TOakDoorTurtling(s, player), event);

                case "glass_wall" -> player.game.turn(new TGlassWall(s, player), event);
                case "blue_glass_wall" -> player.game.turn(new TBlueGlassWall(s, player), event);
                case "orange_glass_wall" -> player.game.turn(new TOrangeGlassWall(s, player), event);
                case "pink_glass_wall" -> player.game.turn(new TPinkGlassWall(s, player), event);
                case "red_glass_wall" -> player.game.turn(new TRedGlassWall(s, player), event);
            }
        }
        if (player.game instanceof DefaultGame) {
            switch (structure.name) {
                case "daylight_sensor_line" -> player.game.turn(new TDaylightSensorLine(s, player), event);
                case "pumpkin_wall" -> player.game.turn(new TPumpkinWall(s, player), event);
                case "redstone_wall" -> player.game.turn(new TRedstoneWall(s, player), event);

                case "green_glass_wall" -> player.game.turn(new TGreenGlassWall(s, player), event);
                case "white_glass_wall" -> player.game.turn(new TWhiteGlassWall(s, player), event);

                case "amethyst-warp" -> player.game.turn(new TAmethystWarp(s, w, player), event);
                case "cliff-warp" -> player.game.turn(new TCliffWarp(s, w, player), event);
                case "desert-warp" -> player.game.turn(new TDesertWarp(s, w, player), event);
                case "end-warp" -> player.game.turn(new TEndWarp(s, w, player), event);
                case "frozen-warp" -> player.game.turn(new TFrozenWarp(s, w, player), event);
                case "meadow-warp" -> player.game.turn(new TMeadowWarp(s, w, player), event);
                case "mushroom-warp" -> player.game.turn(new TMushroomWarp(s, w, player), event);
                case "nerd-warp" -> player.game.turn(new TNerdWarp(s, w, player), event);
                case "nether-warp" -> player.game.turn(new TNetherWarp(s, w, player), event);
                case "ocean-warp" -> player.game.turn(new TOceanWarp(s, w, player), event);
                case "redstone-warp" -> player.game.turn(new TRedstoneWarp(s, w, player), event);
                case "sun-warp" -> player.game.turn(new TSunWarp(s, w, player), event);
                case "void-warp" -> player.game.turn(new TVoidWarp(s, w, player), event);
                case "wood-warp" -> player.game.turn(new TWoodWarp(s, w, player), event);
            }
        }
        if (player.game instanceof LegacyGame) {
            switch (structure.name) {
                case "legacy-pumpkin_wall" -> player.game.turn(new LTPumpkinWall(s, player), event);
                case "legacy-daylight_sensor_strip" -> player.game.turn(new LTDaylightSensorStrip(s, player), event);
                case "legacy-redstone_block_wall" -> player.game.turn(new LTRedstoneBlockWall(s, player), event);
                case "legacy-nether_portal" -> LTNetherPortal.await(player.game);

                case "white_glass_wall" -> player.game.turn(new TWhiteGlassWall(s, player), event);
                case "light_blue_glass_wall" -> player.game.turn(new LTLightBlueGlassWall(s, player), event);

                case "legacy-aether-warp" -> player.game.turn(new LTAetherWarp(s, w, player), event);
                case "legacy-amethyst-warp" -> player.game.turn(new LTAmethystWarp(s, w, player), event);
                case "legacy-book-warp" -> player.game.turn(new LTBookWarp(s, w, player), event);
                case "legacy-cliff-warp" -> player.game.turn(new LTCliffWarp(s, w, player), event);
                case "legacy-end-warp" -> player.game.turn(new LTEndWarp(s, w, player), event);
                case "legacy-ice-warp" -> player.game.turn(new LTIceWarp(s, w, player), event);
                case "legacy-mushroom-warp" -> player.game.turn(new LTMushroomWarp(s, w, player), event);
                case "legacy-nether-warp" -> player.game.turn(new LTNetherWarp(s, w, player), event);
                case "legacy-redstone-warp" -> player.game.turn(new LTRedstoneWarp(s, w, player), event);
                case "legacy-snow-warp" -> player.game.turn(new LTSnowWarp(s, w, player), event);
                case "legacy-sun-warp" -> player.game.turn(new LTSunWarp(s, w, player), event);
                case "legacy-underwater-warp" -> player.game.turn(new LTUnderwaterWarp(s, w, player), event);
                case "legacy-void-warp" -> player.game.turn(new LTVoidWarp(s, w, player), event);
                case "legacy-wood-warp" -> player.game.turn(new LTWoodWarp(s, w, player), event);

                case "legacy-heaven-warp" -> player.game.turn(Randomizer.boolByChance(55) ? new LTHeavenWarp(s, w, player) : new LTHellWarp(s, w, player), event);
                case "legacy-hell-warp" -> player.game.turn(Randomizer.boolByChance(55) ? new LTHellWarp(s, w, player) : new LTHeavenWarp(s, w, player), event);
            }
        }
        if (player.game instanceof PersonGame) {
            switch (structure.name) {
                case "candle_circle" -> player.game.turn(new PTCandleCircle(s, player), event);
                case "pumpkin_wall" -> player.game.turn(new PTPumpkinWall(s, player), event);
                case "stone_wall" -> player.game.turn(new PTStoneWall(s, player), event);
                case "torch_circle" -> player.game.turn(new PTTorchCircle(s, player), event);
                case "oak_door_turtling" -> player.game.turn(new PTTurtling(s, player), event);
                case "wood_wall" -> player.game.turn(new PTWoodWall(s, player), event);

                case "person-amethyst-warp" -> player.game.turn(new PTAmethystWarp(s, w, player), event);
                case "person-fire-warp" -> player.game.turn(new PTFireWarp(s, w, player), event);
                case "person-ice-warp" -> player.game.turn(new PTIceWarp(s, w, player), event);
                case "person-snow-warp" -> player.game.turn(new PTSnowWarp(s, w, player), event);
                case "person-villager-warp" -> player.game.turn(new PTVillagerWarp(s, w, player), event);
                case "person-void-warp" -> player.game.turn(new PTVoidWarp(s, w, player), event);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        NeoPlayer player = GameManager.player(event.getPlayer(), true);
        if (unusable(event.getPlayer(), player, false, event, "You cannot interact right now!")) return;

        switch (player.game) {
            case DefaultGame ignored -> {
                if (unusable(event.getPlayer(), player, true, event, "You cannot interact right now!")) return;
                if (event.getMaterial().name().endsWith("_SPAWN_EGG")) {
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
                } else if (event.getClickedBlock() != null && event.getClickedBlock().getBlockData() instanceof NoteBlock noteBlock) {
                    player.game.turn(new TNoteBlock(noteBlock, event.getClickedBlock().getLocation(), player), event);
                }
            }
            case CardGame ignored -> {
                if (player.game.history().isEmpty()) return;
                if (player.game.history().getLast() instanceof InteractableCardTurn interactable &&
                        player == interactable.player() && interactable.data().getLocation().equals(event.getClickedBlock().getLocation())) {
                    interactable.interact();
                }
            }
            case LegacyGame ignored -> {
                if (event.getClickedBlock() != null && event.getClickedBlock().getBlockData() instanceof NoteBlock noteBlock) {
                    player.game.turn(new LTNoteBlock(noteBlock, event.getClickedBlock().getLocation(), player), event);
                } else if (event.getClickedBlock() != null && event.getClickedBlock().getBlockData() instanceof SeaPickle seaPickle) {
                    if (seaPickle.getPickles() == seaPickle.getMaximumPickles())
                        player.game.turn(new LTSeaPickle(event.getClickedBlock(), player), event);
                }
            }
            default -> {}
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        if (event.getEntity() instanceof Painting painting) {
            NeoPlayer player = GameManager.player(event.getPlayer(), true);
            if (unusable(event.getPlayer(), player, false, event, "You cannot place paintings right now!")) return;

            if (player.game instanceof PersonGame)
                player.game.turn(new PTPainting(painting, player), event);
        }
    }


    @EventHandler(ignoreCancelled = true)
    public void onEntityPlace(@NotNull EntityPlaceEvent event) {
        if (event.getEntity() instanceof Boat boat) {
            NeoPlayer player = GameManager.player(event.getPlayer(), true);
            if (unusable(event.getPlayer(), player, true, event, "You cannot place boats right now!")) return;

            if (player.game instanceof DefaultGame)
                player.game.turn(new TBoat(boat, player), event);
        } else if (event.getEntity() instanceof ArmorStand armorStand) {
            NeoPlayer player = GameManager.player(event.getPlayer(), true);
            if (unusable(event.getPlayer(), player, true, event, "You cannot place armor stands right now!")) return;

            if (player.game instanceof PersonGame)
                player.game.turn(new PTArmorStand(armorStand, player), event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerItemConsume(@NotNull PlayerItemConsumeEvent event) {
        if (event.getItem().getType() == Material.CHORUS_FRUIT) {
            NeoPlayer player = GameManager.player(event.getPlayer(), true);
            if (unusable(event.getPlayer(), player, true, event, "You cannot consume chorus fruits right now!")) return;

            event.setCancelled(true); // Prevent vanilla teleportation!

            if (player.game instanceof DefaultGame)
                player.game.turn(new TChorusFruit(event.getItem(), event.getPlayer().getLocation(), player), event);
        } else if (event.getItem().getType() == Material.ENCHANTED_GOLDEN_APPLE) {
            NeoPlayer player = GameManager.player(event.getPlayer(), true);
            if (unusable(event.getPlayer(), player, true, event, "You cannot eat enchanted golden apples right now!")) return;

            if (player.game instanceof LegacyGame)
                player.game.turn(new LTEnchantedGoldenApple(event.getItem(), event.getPlayer().getLocation(), player), event);
        } else if (event.getItem().getType() == Material.SUSPICIOUS_STEW) {
            NeoPlayer player = GameManager.player(event.getPlayer(), true);
            if (unusable(event.getPlayer(), player, true, event, "You cannot eat enchanted suspicious stew right now!")) return;

            if (player.game instanceof PersonGame)
                player.game.turn(new PTSuspiciousStew(event.getItem(), event.getPlayer().getLocation(), player), event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTransform(@NotNull EntityTransformEvent event) {
        if (event.getTransformReason() == EntityTransformEvent.TransformReason.PIGLIN_ZOMBIFIED) {
            Game game = GameManager.game(event.getEntity().getScoreboardTags());
            if (game instanceof DefaultGame) {
                NeoPlayer player = game.currentPlayer();
                new TPiglin(game.currentPlayer()).buffs().forEach(b -> b.apply(player));
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionSplash(@NotNull PotionSplashEvent event) {
        if (event.getPotion().getShooter() instanceof Player p) {
            NeoPlayer player = GameManager.player(p, true);
            if (unusable(p, player, true, new CancellableImpl(), "You cannot use potions right now!")) return;

            if (player.game instanceof DefaultGame) {
                PotionMeta meta = event.getPotion().getPotionMeta();
                if (meta.hasBasePotionType()) {
                    switch (meta.getBasePotionType()) {
                        case WATER -> player.game.turn(new TSplashWaterBottle(event.getPotion(), event.getPotion().getLocation(), player), event);
                        case STRONG_LEAPING -> player.game.turn(new TSplashJumpBoostPotion(event.getPotion(), event.getPotion().getLocation(), player), event);
                    }
                } else if (meta.hasCustomEffect(PotionEffectType.LEVITATION)) {
                    player.game.turn(new TSplashLevitationPotion(event.getPotion(), event.getPotion().getLocation(), player), event);
                }
            } else if (player.game instanceof PersonGame) {
                player.game.turn(new PTSplashPotion(event.getPotion(), event.getPotion().getLocation(), player), event);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onProjectileHit(@NotNull ProjectileHitEvent event) {
        if (event.getEntity().getShooter() instanceof Player p) {
            NeoPlayer player = GameManager.player(p, true);
            if (unusable(p, player, true, new CancellableImpl(), "You cannot throw things right now!")) return;

            switch (player.game) {
                case DefaultGame ignored -> {
                    if (event.getEntity() instanceof Snowball snowball) {
                        player.game.turn(new TSnowball(snowball, snowball.getLocation(), player), event);
                    } else if (event.getEntity() instanceof EnderPearl enderPearl) {
                        player.game.turn(new TEnderPearl(enderPearl, enderPearl.getLocation(), player), event);
                    }
                }
                case LegacyGame ignored -> {
                    if (event.getEntity() instanceof Trident trident)
                        player.game.turn(new LTLightningTrident(trident.getItemStack(), trident.getLocation(), player), event);
                }
                case PersonGame ignored -> {
                    if (event.getEntity() instanceof Snowball snowball)
                        player.game.turn(new PTSnowball(snowball, snowball.getLocation(), player), event);
                }
                default -> {}
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDropItem(@NotNull PlayerDropItemEvent event) {
        NeoPlayer player = GameManager.player(event.getPlayer(), true);
        if (unusable(event.getPlayer(), player, true, event, "You cannot drop items right now!")) return;

        if (player.game instanceof DefaultGame && event.getItemDrop().getItemStack().getType() == Material.IRON_SHOVEL)
            player.game.turn(new TIronShovel(event.getItemDrop().getItemStack(), event.getItemDrop().getLocation(), player), event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerNameEntity(@NotNull PlayerNameEntityEvent event) {
        NeoPlayer player = GameManager.player(event.getPlayer(), true);
        if (unusable(event.getPlayer(), player, true, event, "You cannot name mobs right now!")) return;

        if (event.getEntity() instanceof Sheep && event.getName().equals(Component.text("Dinnerbone")))
            player.game.turn(new LTDinnerboneTag(event.getPlayer().getActiveItem(), event.getEntity().getLocation(), player), event);
    }

    @EventHandler(ignoreCancelled = true)
    public void onTNTPrime(@NotNull TNTPrimeEvent event) {
        if (event.getPrimingEntity() instanceof Player p) {
            NeoPlayer player = GameManager.player(p, true);
            if (unusable(p, player, true, event, "You cannot prime tnt right now!")) return;

            player.game.turn(new LTTnt(event.getBlock(), player), event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPortalCreate(@NotNull PortalCreateEvent event) {
        if (event.getReason() == PortalCreateEvent.CreateReason.FIRE && event.getEntity() instanceof Player p) {
            NeoPlayer player = GameManager.player(p, true);
            if (unusable(p, player, false, event, "You cannot ignite portals right now!")) return;

            if (player.game.hasModifier(Modifiers.G_LEGACY_PORTAL_AWAIT))
                player.game.turn(new LTNetherPortal(event.getBlocks(), event.getEntity().getLocation(), player), event);
        }
    }


    private boolean unusable(Player fallbackPlayer, NeoPlayer player, boolean requireTurning, Cancellable event, String message) {
        if (player == null) {
            if (fallbackPlayer.getGameMode() != GameMode.CREATIVE) // Allow builders and admins to do this.
                event.setCancelled(true);
            return true;
        }

        if (!player.game.started() || (requireTurning && player.game.currentPlayer() != player)) {
            player.sendMessage(Component.text(message, Colors.NEGATIVE));
            event.setCancelled(true);
            return true;
        }
        return false;
    }
}
