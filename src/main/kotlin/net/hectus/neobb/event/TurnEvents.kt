package net.hectus.neobb.event

import com.marcpg.libpg.event.CancellableImpl
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.game.mode.CardGame
import net.hectus.neobb.game.mode.DefaultGame
import net.hectus.neobb.game.mode.HectusGame
import net.hectus.neobb.game.mode.PersonGame
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StructureManager
import net.hectus.neobb.modes.turn.card_game.*
import net.hectus.neobb.modes.turn.default_game.block.*
import net.hectus.neobb.modes.turn.default_game.item.TChorusFruit
import net.hectus.neobb.modes.turn.default_game.item.TIronShovel
import net.hectus.neobb.modes.turn.default_game.mob.*
import net.hectus.neobb.modes.turn.default_game.other.TBoat
import net.hectus.neobb.modes.turn.default_game.other.TNoteBlock
import net.hectus.neobb.modes.turn.default_game.structure.*
import net.hectus.neobb.modes.turn.default_game.structure.glass_wall.*
import net.hectus.neobb.modes.turn.default_game.throwable.*
import net.hectus.neobb.modes.turn.default_game.warp.*
import net.hectus.neobb.modes.turn.person_game.block.*
import net.hectus.neobb.modes.turn.person_game.item.PTSuspiciousStew
import net.hectus.neobb.modes.turn.person_game.other.PTArmorStand
import net.hectus.neobb.modes.turn.person_game.other.PTPainting
import net.hectus.neobb.modes.turn.person_game.structure.*
import net.hectus.neobb.modes.turn.person_game.throwable.PTSnowball
import net.hectus.neobb.modes.turn.person_game.throwable.PTSplashPotion
import net.hectus.neobb.modes.turn.person_game.warp.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.asCord
import net.hectus.neobb.util.bukkitRunLater
import net.kyori.adventure.text.Component
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.entity.*
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityPlaceEvent
import org.bukkit.event.entity.EntityTransformEvent
import org.bukkit.event.entity.PotionSplashEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.hanging.HangingPlaceEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType

class TurnEvents: Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.block.type == Material.BLACK_STAINED_GLASS_PANE)
            event.isCancelled = true

        val player = GameManager.player(event.player, true)
        if (unusable(event.player, player, true, event, "You cannot place blocks right now!")) return

        val block = event.block

        player!!.game.resetTurnCountdown()
        if (player.game.info.hasStructures && StructureManager.MATERIALS.contains(block.type)) {
            if (player.game.arena.placedBlocks == 0) {
                bukkitRunLater(30) {
                    if (player.game.arena.placedBlocks > 1) {
                        handleStructure(event, block, player)
                    } else {
                        blockTurn(event, block, player)
                    }
                }
            } else {
                handleStructure(event, block, player)
            }
        } else {
            blockTurn(event, block, player)
        }
    }

    private fun blockTurn(event: BlockPlaceEvent, block: Block, player: NeoPlayer) {
        val cord = block.location.asCord()
        when (player.game) {
            is CardGame -> {
                when (block.type) {
                    Material.CHEST -> player.game.turn(CTChest(block, cord, player), event)
                    Material.DAYLIGHT_DETECTOR -> player.game.turn(CTDaylightDetector(block, cord, player), event)
                    Material.FLOWER_POT -> player.game.turn(CTFlowerPot(block, cord, player), event)
                    Material.JACK_O_LANTERN -> player.game.turn(CTJackOLantern(block, cord, player), event)
                    Material.OAK_DOOR -> player.game.turn(CTOakDoor(block, cord, player), event)
                    Material.OAK_FENCE_GATE -> player.game.turn(CTOakFenceGate(block, cord, player), event)
                    Material.OAK_TRAPDOOR -> player.game.turn(CTOakTrapdoor(block, cord, player), event)
                    Material.POINTED_DRIPSTONE -> player.game.turn(CTPointedDripstone(block, cord, player), event)
                    Material.REDSTONE_LAMP -> player.game.turn(CTRedstoneLamp(block, cord, player), event)
                    Material.TORCH -> player.game.turn(CTTorch(block, cord, player), event)
                    Material.WAXED_EXPOSED_CUT_COPPER_STAIRS -> player.game.turn(CTWaxedExposedCutCopperStairs(block, cord, player), event)

                    else -> event.isCancelled = true
                }
            }
            is HectusGame -> {
                when (block.type) {
                    Material.BEE_NEST -> player.game.turn(TBeeNest(block, cord, player), event)
                    Material.BLACK_WOOL -> player.game.turn(TBlackWool(block, cord, player), event)
                    Material.BLUE_BED -> player.game.turn(TBlueBed(block, cord, player), event)
                    Material.BLUE_ICE -> player.game.turn(TBlueIce(block, cord, player), event)
                    Material.BRAIN_CORAL_BLOCK -> player.game.turn(TBrainCoralBlock(block, cord, player), event)
                    Material.CAMPFIRE -> player.game.turn(TCampfire(block, cord, player), event)
                    Material.CAULDRON -> player.game.turn(TCauldron(block, cord, player), event)
                    Material.COMPOSTER -> player.game.turn(TComposter(block, cord, player), event)
                    Material.CYAN_CARPET -> player.game.turn(TCyanCarpet(block, cord, player), event)
                    Material.DRAGON_HEAD -> player.game.turn(TDragonHead(block, cord, player), event)
                    Material.DRIED_KELP_BLOCK -> player.game.turn(TDriedKelpBlock(block, cord, player), event)
                    Material.FIRE -> player.game.turn(TFire(block, cord, player), event)
                    Material.FIRE_CORAL -> player.game.turn(TFireCoral(block, cord, player), event)
                    Material.FIRE_CORAL_FAN -> player.game.turn(TFireCoralFan(block, cord, player), event)
                    Material.GOLD_BLOCK -> player.game.turn(TGoldBlock(block, cord, player), event)
                    Material.GREEN_BED -> player.game.turn(TGreenBed(block, cord, player), event)
                    Material.GREEN_CARPET -> player.game.turn(TGreenCarpet(block, cord, player), event)
                    Material.GREEN_WOOL -> player.game.turn(TGreenWool(block, cord, player), event)
                    Material.HAY_BLOCK -> player.game.turn(THayBlock(block, cord, player), event)
                    Material.HONEY_BLOCK -> player.game.turn(THoneyBlock(block, cord, player), event)
                    Material.HORN_CORAL -> player.game.turn(THornCoral(block, cord, player), event)
                    Material.IRON_TRAPDOOR -> player.game.turn(TIronTrapdoor(block, cord, player), event)
                    Material.LAVA -> player.game.turn(TLava(block, cord, player), event)
                    Material.LEVER -> player.game.turn(TLever(block, cord, player), event)
                    Material.LIGHTNING_ROD -> player.game.turn(TLightningRod(block, cord, player), event)
                    Material.LIGHT_BLUE_WOOL -> player.game.turn(TLightBlueWool(block, cord, player), event)
                    Material.MAGENTA_GLAZED_TERRACOTTA -> player.game.turn(TMagentaGlazedTerracotta(block, cord, player), event)
                    Material.MAGMA_BLOCK -> player.game.turn(TMagmaBlock(block, cord, player), event)
                    Material.MANGROVE_ROOTS -> player.game.turn(TMangroveRoots(block, cord, player), event)
                    Material.NETHERRACK -> player.game.turn(TNetherrack(block, cord, player), event)
                    Material.OAK_FENCE_GATE -> player.game.turn(TFenceGate(block, cord, player), event)
                    Material.OAK_STAIRS -> player.game.turn(TOakStairs(block, cord, player), event)
                    Material.ORANGE_WOOL -> player.game.turn(TOrangeWool(block, cord, player), event)
                    Material.PACKED_ICE -> player.game.turn(TPackedIce(block, cord, player), event)
                    Material.PINK_BED -> player.game.turn(TPinkBed(block, cord, player), event)
                    Material.POWDER_SNOW -> player.game.turn(TPowderSnow(block, cord, player), event)
                    Material.PURPLE_WOOL -> player.game.turn(TPurpleWool(block, cord, player), event)
                    Material.RED_BED -> player.game.turn(TRedBed(block, cord, player), event)
                    Material.RED_CARPET -> player.game.turn(TRedCarpet(block, cord, player), event)
                    Material.REPEATER -> player.game.turn(TRepeater(block, cord, player), event)
                    Material.RESPAWN_ANCHOR -> player.game.turn(TRespawnAnchor(block, cord, player), event)
                    Material.SCULK -> player.game.turn(TSculk(block, cord, player), event)
                    Material.SEA_LANTERN -> player.game.turn(TSeaLantern(block, cord, player), event)
                    Material.SOUL_SAND -> player.game.turn(TSoulSand(block, cord, player), event)
                    Material.SPONGE -> player.game.turn(TSponge(block, cord, player), event)
                    Material.SPRUCE_LEAVES -> player.game.turn(TSpruceLeaves(block, cord, player), event)
                    Material.SPRUCE_TRAPDOOR -> player.game.turn(TSpruceTrapdoor(block, cord, player), event)
                    Material.STONECUTTER -> player.game.turn(TStonecutter(block, cord, player), event)
                    Material.VERDANT_FROGLIGHT -> player.game.turn(TVerdantFroglight(block, cord, player), event)
                    Material.WATER -> player.game.turn(TWater(block, cord, player), event)
                    Material.WHITE_WOOL -> player.game.turn(TWhiteWool(block, cord, player), event)
                    Material.DIRT, Material.FLOWER_POT -> {}
                    else -> event.isCancelled = true
                }
            }
            is PersonGame -> {
                when (block.type) {
                    Material.LIGHT_BLUE_CARPET -> player.game.turn(PTLightBlueCarpet(block, cord, player), event)
                    Material.DAYLIGHT_DETECTOR -> player.game.turn(PTDaylightDetector(block, cord, player), event)
                    Material.RED_WOOL -> player.game.turn(PTRedWool(block, cord, player), event)
                    Material.BEE_NEST -> player.game.turn(PTBeeNest(block, cord, player), event)
                    Material.CHERRY_PRESSURE_PLATE -> player.game.turn(PTCherryPressurePlate(block, cord, player), event)
                    Material.LEVER -> player.game.turn(PTLever(block, cord, player), event)
                    Material.DIAMOND_BLOCK -> player.game.turn(PTDiamondBlock(block, cord, player), event)
                    Material.PINK_CARPET -> player.game.turn(PTPinkCarpet(block, cord, player), event)
                    Material.BIRCH_LOG -> player.game.turn(PTBirchLog(block, cord, player), event)
                    Material.CAKE -> player.game.turn(PTCake(block, cord, player), event)
                    Material.BAMBOO_BUTTON -> player.game.turn(PTBambooButton(block, cord, player), event)
                    Material.ORANGE_WOOL -> player.game.turn(PTOrangeWool(block, cord, player), event)
                    Material.BRAIN_CORAL -> player.game.turn(PTBrainCoral(block, cord, player), event)
                    Material.GLOWSTONE -> player.game.turn(PTGlowstone(block, cord, player), event)
                    Material.RED_STAINED_GLASS -> player.game.turn(PTRedStainedGlass(block, cord, player), event)
                    Material.CHERRY_BUTTON -> player.game.turn(PTCherryButton(block, cord, player), event)
                    Material.BLUE_CONCRETE -> player.game.turn(PTBlueConcrete(block, cord, player), event)
                    Material.GREEN_CARPET -> player.game.turn(PTGreenCarpet(block, cord, player), event)
                    Material.IRON_TRAPDOOR -> player.game.turn(PTIronTrapdoor(block, cord, player), event)
                    Material.BARREL -> player.game.turn(PTBarrel(block, cord, player), event)
                    Material.GRAY_WOOL -> player.game.turn(PTGrayWool(block, cord, player), event)
                    Material.BROWN_STAINED_GLASS -> player.game.turn(PTBrownStainedGlass(block, cord, player), event)
                    Material.GOLD_BLOCK -> player.game.turn(PTGoldBlock(block, cord, player), event)
                    Material.VERDANT_FROGLIGHT -> player.game.turn(PTVerdantFroglight(block, cord, player), event)
                    Material.FLETCHING_TABLE -> player.game.turn(PTFletchingTable(block, cord, player), event)
                    Material.HONEY_BLOCK -> player.game.turn(PTHoneyBlock(block, cord, player), event)
                    Material.WHITE_STAINED_GLASS -> player.game.turn(PTWhiteStainedGlass(block, cord, player), event)
                    Material.BLUE_ICE -> player.game.turn(PTBlueIce(block, cord, player), event)
                    Material.BLUE_STAINED_GLASS -> player.game.turn(PTBlueStainedGlass(block, cord, player), event)
                    Material.POINTED_DRIPSTONE -> player.game.turn(PTDripstone(block, cord, player), event)
                    Material.NOTE_BLOCK -> player.game.turn(PTNoteBlock(block, cord, player), event)
                    Material.BLACK_CARPET -> player.game.turn(PTBlackCarpet(block, cord, player), event)
                    Material.OAK_FENCE_GATE -> player.game.turn(PTFenceGate(block, cord, player), event)
                    Material.AMETHYST_BLOCK -> player.game.turn(PTAmethystBlock(block, cord, player), event)
                    Material.WHITE_WOOL -> player.game.turn(PTWhiteWool(block, cord, player), event)
                    Material.STONECUTTER -> player.game.turn(PTStonecutter(block, cord, player), event)
                    Material.SEA_LANTERN -> player.game.turn(PTSeaLantern(block, cord, player), event)
                    Material.PURPLE_WOOL -> player.game.turn(PTPurpleWool(block, cord, player), event)
                    else -> event.isCancelled = true
                }
            }
            else -> event.isCancelled = true
        }
    }

    private fun handleStructure(event: BlockPlaceEvent, block: Block, player: NeoPlayer) {
        val structure = StructureManager.match(player.game.arena) ?: return

        val placed = PlacedStructure(structure, block)
        val cord = placed.lastBlock.location.asCord()
        if (player.game is HectusGame) {
            when (structure.name) {
                "default.iron_bar_jail" -> player.game.turn(TIronBarJail(placed, cord, player), event)
                "default.oak_door_turtling" -> player.game.turn(TOakDoorTurtling(placed, cord, player), event)
                "default.glass_wall.default" -> player.game.turn(TGlassWall(placed, cord, player), event)
                "default.glass_wall.blue" -> player.game.turn(TBlueGlassWall(placed, cord, player), event)
                "default.glass_wall.orange" -> player.game.turn(TOrangeGlassWall(placed, cord, player), event)
                "default.glass_wall.pink" -> player.game.turn(TPinkGlassWall(placed, cord, player), event)
                "default.glass_wall.red" -> player.game.turn(TRedGlassWall(placed, cord, player), event)
            }
        }
        if (player.game is DefaultGame) {
            when (structure.name) {
                "default.daylight_sensor_line" -> player.game.turn(TDaylightSensorLine(placed, cord, player), event)
                "default.pumpkin_wall" -> player.game.turn(TPumpkinWall(placed, cord, player), event)
                "default.redstone_wall" -> player.game.turn(TRedstoneWall(placed, cord, player), event)
                "default.glass_wall.green" -> player.game.turn(TGreenGlassWall(placed, cord, player), event)
                "default.glass_wall.white" -> player.game.turn(TWhiteGlassWall(placed, cord, player), event)
                "default.warp.amethyst" -> player.game.turn(TAmethystWarp(placed, cord, player), event)
                "default.warp.cliff" -> player.game.turn(TCliffWarp(placed, cord, player), event)
                "default.warp.desert" -> player.game.turn(TDesertWarp(placed, cord, player), event)
                "default.warp.end" -> player.game.turn(TEndWarp(placed, cord, player), event)
                "default.warp.frozen" -> player.game.turn(TFrozenWarp(placed, cord, player), event)
                "default.warp.meadow" -> player.game.turn(TMeadowWarp(placed, cord, player), event)
                "default.warp.mushroom" -> player.game.turn(TMushroomWarp(placed, cord, player), event)
                "default.warp.nerd" -> player.game.turn(TNerdWarp(placed, cord, player), event)
                "default.warp.nether" -> player.game.turn(TNetherWarp(placed, cord, player), event)
                "default.warp.ocean" -> player.game.turn(TOceanWarp(placed, cord, player), event)
                "default.warp.redstone" -> player.game.turn(TRedstoneWarp(placed, cord, player), event)
                "default.warp.sun" -> player.game.turn(TSunWarp(placed, cord, player), event)
                "default.warp.void" -> player.game.turn(TVoidWarp(placed, cord, player), event)
                "default.warp.wood" -> player.game.turn(TWoodWarp(placed, cord, player), event)
            }
        }
        if (player.game is PersonGame) {
            when (structure.name) {
                "person.candle_circle" -> player.game.turn(PTCandleCircle(placed, cord, player), event)
                "person.pumpkin_wall" -> player.game.turn(PTPumpkinWall(placed, cord, player), event)
                "person.stone_wall" -> player.game.turn(PTStoneWall(placed, cord, player), event)
                "person.torch_circle" -> player.game.turn(PTTorchCircle(placed, cord, player), event)
                "person.turtling" -> player.game.turn(PTTurtling(placed, cord, player), event)
                "person.wood_wall" -> player.game.turn(PTWoodWall(placed, cord, player), event)
                "person.warp.amethyst" -> player.game.turn(PTAmethystWarp(placed, cord, player), event)
                "person.warp.fire" -> player.game.turn(PTFireWarp(placed, cord, player), event)
                "person.warp.ice" -> player.game.turn(PTIceWarp(placed, cord, player), event)
                "person.warp.snow" -> player.game.turn(PTSnowWarp(placed, cord, player), event)
                "person.warp.villager" -> player.game.turn(PTVillagerWarp(placed, cord, player), event)
                "person.warp.void" -> player.game.turn(PTVoidWarp(placed, cord, player), event)
            }
        }
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        val player = GameManager.player(event.player, true)
        if (unusable(event.player, player, false, event, "You cannot interact right now!")) return

        when (player!!.game) {
            is DefaultGame -> {
                if (unusable(event.player, player, true, event, "You cannot interact right now!")) return
                val blockData = event.clickedBlock!!.blockData
                if (event.material.name.endsWith("_SPAWN_EGG")) {
                    val loc = event.interactionPoint!!
                    when (event.material) {
                        Material.AXOLOTL_SPAWN_EGG -> player.game.turn(TAxolotl(loc.world.spawn(loc, Axolotl::class.java), loc.asCord(), player), event)
                        Material.BEE_SPAWN_EGG -> player.game.turn(TBee(loc.world.spawn(loc, Bee::class.java), loc.asCord(), player), event)
                        Material.BLAZE_SPAWN_EGG -> player.game.turn(TBlaze(loc.world.spawn(loc, Blaze::class.java), loc.asCord(), player), event)
                        Material.EVOKER_SPAWN_EGG -> player.game.turn(TEvoker(loc.world.spawn(loc, Evoker::class.java), loc.asCord(), player), event)
                        Material.PHANTOM_SPAWN_EGG -> player.game.turn(TPhantom(loc.world.spawn(loc, Phantom::class.java), loc.asCord(), player), event)
                        Material.PIGLIN_SPAWN_EGG -> player.game.turn(TPiglin(loc.world.spawn(loc, Piglin::class.java), loc.asCord(), player), event)
                        Material.POLAR_BEAR_SPAWN_EGG -> player.game.turn(TPolarBear(loc.world.spawn(loc, PolarBear::class.java), loc.asCord(), player), event)
                        Material.PUFFERFISH_SPAWN_EGG -> player.game.turn(TPufferfish(loc.world.spawn(loc, PufferFish::class.java), loc.asCord(), player), event)
                        Material.SHEEP_SPAWN_EGG -> player.game.turn(TSheep(loc.world.spawn(loc, Sheep::class.java), loc.asCord(), player), event)
                        else -> {}
                    }
                    event.isCancelled = true
                } else if (event.clickedBlock != null && blockData is NoteBlock) {
                    player.game.turn(TNoteBlock(blockData, event.clickedBlock!!.location.asCord(), player), event)
                }
            }
            is CardGame -> {
                if (player.game.history.isEmpty()) return
                val turn = player.game.history.last()
                if (turn is InteractableCardTurn && player == turn.player && turn.data!!.location == event.clickedBlock!!.location)
                    turn.interact()
            }
            else -> {}
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onHangingPlace(event: HangingPlaceEvent) {
        val entity = event.entity
        if (entity is Painting) {
            val player = GameManager.player(event.player!!, true)
            if (unusable(event.player!!, player, false, event, "You cannot place paintings right now!")) return

            if (player!!.game is PersonGame)
                player.game.turn(PTPainting(entity, entity.location.asCord(), player), event)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityPlace(event: EntityPlaceEvent) {
        val entity = event.entity
        if (entity is Boat) {
            val player = GameManager.player(event.player!!, true)
            if (unusable(event.player!!, player, true, event, "You cannot place boats right now!")) return

            if (player!!.game is DefaultGame)
                player.game.turn(TBoat(entity, entity.location.asCord(), player), event)
        } else if (entity is ArmorStand) {
            val player = GameManager.player(event.player!!, true)
            if (unusable(event.player!!, player, true, event, "You cannot place armor stands right now!")) return

            if (player!!.game is PersonGame)
                player.game.turn(PTArmorStand(entity, entity.location.asCord(), player), event)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
        if (event.item.type == Material.CHORUS_FRUIT) {
            val player = GameManager.player(event.player, true)
            if (unusable(event.player, player, true, event, "You cannot consume chorus fruits right now!")) return

            event.isCancelled = true // Prevent vanilla teleportation!

            if (player!!.game is DefaultGame)
                player.game.turn(TChorusFruit(event.item, event.player.location.asCord(), player), event)
        } else if (event.item.type == Material.SUSPICIOUS_STEW) {
            val player = GameManager.player(event.player, true)
            if (unusable(event.player, player, true, event, "You cannot eat enchanted suspicious stew right now!")) return

            if (player!!.game is PersonGame)
                player.game.turn(PTSuspiciousStew(event.item, event.player.location.asCord(), player), event)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityTransform(event: EntityTransformEvent) {
        if (event.transformReason == EntityTransformEvent.TransformReason.PIGLIN_ZOMBIFIED) {
            val game = GameManager[event.entity.scoreboardTags]
            if (game is DefaultGame) {
                val player = game.currentPlayer()
                TPiglin(null, null, player).buffs().forEach { it.apply(player) }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPotionSplash(event: PotionSplashEvent) {
        val p = event.potion.shooter
        if (p is Player) {
            val player = GameManager.player(p, true)
            if (unusable(p, player, true, CancellableImpl(), "You cannot use potions right now!")) return

            if (player!!.game is DefaultGame) {
                val meta = event.potion.potionMeta
                if (meta.hasBasePotionType()) {
                    when (meta.basePotionType) {
                        PotionType.WATER -> player.game.turn(TSplashWaterBottle(event.potion, event.potion.location.asCord(), player), event)
                        PotionType.STRONG_LEAPING -> player.game.turn(TSplashJumpBoostPotion(event.potion, event.potion.location.asCord(), player), event)
                        else -> {}
                    }
                } else if (meta.hasCustomEffect(PotionEffectType.LEVITATION)) {
                    player.game.turn(TSplashLevitationPotion(event.potion, event.potion.location.asCord(), player), event)
                }
            } else if (player.game is PersonGame) {
                player.game.turn(PTSplashPotion(event.potion, event.potion.location.asCord(), player), event)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onProjectileHit(event: ProjectileHitEvent) {
        val p = event.entity.shooter
        if (p is Player) {
            val player = GameManager.player(p, true)
            if (unusable(p, player, true, CancellableImpl(), "You cannot throw things right now!")) return

            val entity = event.entity
            when (player!!.game) {
                is DefaultGame -> {
                    if (entity is Snowball) {
                        player.game.turn(TSnowball(entity, entity.location.asCord(), player), event)
                    } else if (event.entity is EnderPearl) {
                        player.game.turn(TEnderPearl(entity, entity.location.asCord(), player), event)
                    }
                }
                is PersonGame -> {
                    if (entity is Snowball)
                        player.game.turn(PTSnowball(entity, entity.location.asCord(), player), event)
                }
                else -> {}
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val player = GameManager.player(event.player, true)
        if (!(player?.game?.started ?: false)) return // Allow dropping in shop before game started.

        if (unusable(event.player, player, true, event, "You cannot drop items right now!")) return

        if (player.game is DefaultGame && event.itemDrop.itemStack.type == Material.IRON_SHOVEL)
            player.game.turn(TIronShovel(event.itemDrop.itemStack, event.itemDrop.location.asCord(), player), event)
    }

    private fun unusable(fallbackPlayer: Player, player: NeoPlayer?, requireTurning: Boolean, event: Cancellable, message: String): Boolean {
        if (player == null) {
            if (fallbackPlayer.gameMode != GameMode.CREATIVE) // Allow builders and admins to do this.
                event.isCancelled = true
            return true
        }

        if (!player.game.started || (requireTurning && player.game.currentPlayer() !== player)) {
            player.sendMessage(Component.text(message, Colors.NEGATIVE))
            event.isCancelled = true
            return true
        }
        return false
    }

}
