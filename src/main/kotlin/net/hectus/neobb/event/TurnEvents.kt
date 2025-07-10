package net.hectus.neobb.event

import com.marcpg.libpg.event.CancellableImpl
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.util.bukkitRunLater
import com.marcpg.libpg.util.toCord
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.game.mode.CardGame
import net.hectus.neobb.game.mode.DefaultGame
import net.hectus.neobb.game.mode.HectusGame
import net.hectus.neobb.game.mode.PersonGame
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StructureManager
import net.hectus.neobb.modes.turn.card_game.*
import net.hectus.neobb.modes.turn.default_game.*
import net.hectus.neobb.modes.turn.person_game.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
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
        val cord = block.location.toCord()
        player.game.turn(when (player.game) {
            is CardGame -> when (block.type) {
                Material.CHEST -> ::CTChest
                Material.DAYLIGHT_DETECTOR -> ::CTDaylightDetector
                Material.FLOWER_POT -> ::CTFlowerPot
                Material.JACK_O_LANTERN -> ::CTJackOLantern
                Material.OAK_DOOR -> ::CTOakDoor
                Material.OAK_FENCE_GATE -> ::CTOakFenceGate
                Material.OAK_TRAPDOOR -> ::CTOakTrapdoor
                Material.POINTED_DRIPSTONE -> ::CTPointedDripstone
                Material.REDSTONE_LAMP -> ::CTRedstoneLamp
                Material.TORCH -> ::CTTorch
                Material.WAXED_EXPOSED_CUT_COPPER_STAIRS -> ::CTWaxedExposedCutCopperStairs
                else -> { event.isCancelled = true; null }
            }
            is HectusGame -> when (block.type) {
                Material.BEE_NEST -> ::TBeeNest
                Material.BLACK_WOOL -> ::TBlackWool
                Material.BLUE_BED -> ::TBlueBed
                Material.BLUE_ICE -> ::TBlueIce
                Material.BRAIN_CORAL_BLOCK -> ::TBrainCoralBlock
                Material.CAMPFIRE -> ::TCampfire
                Material.CAULDRON -> ::TCauldron
                Material.COMPOSTER -> ::TComposter
                Material.CYAN_CARPET -> ::TCyanCarpet
                Material.DIRT -> ::TDirt
                Material.DRAGON_HEAD -> ::TDragonHead
                Material.DRIED_KELP_BLOCK -> ::TDriedKelpBlock
                Material.FIRE -> ::TFire
                Material.FIRE_CORAL -> ::TFireCoral
                Material.FIRE_CORAL_FAN -> ::TFireCoralFan
                Material.FLOWER_POT -> ::TFlowerPot
                Material.GOLD_BLOCK -> ::TGoldBlock
                Material.GREEN_BED -> ::TGreenBed
                Material.GREEN_CARPET -> ::TGreenCarpet
                Material.GREEN_WOOL -> ::TGreenWool
                Material.HAY_BLOCK -> ::THayBlock
                Material.HONEY_BLOCK -> ::THoneyBlock
                Material.HORN_CORAL -> ::THornCoral
                Material.IRON_TRAPDOOR -> ::TIronTrapdoor
                Material.LAVA -> ::TLava
                Material.LEVER -> ::TLever
                Material.LIGHTNING_ROD -> ::TLightningRod
                Material.LIGHT_BLUE_WOOL -> ::TLightBlueWool
                Material.MAGENTA_GLAZED_TERRACOTTA -> ::TMagentaGlazedTerracotta
                Material.MAGMA_BLOCK -> ::TMagmaBlock
                Material.MANGROVE_ROOTS -> ::TMangroveRoots
                Material.NETHERRACK -> ::TNetherrack
                Material.OAK_FENCE_GATE -> ::TFenceGate
                Material.OAK_STAIRS -> ::TOakStairs
                Material.ORANGE_WOOL -> ::TOrangeWool
                Material.PACKED_ICE -> ::TPackedIce
                Material.PINK_BED -> ::TPinkBed
                Material.POWDER_SNOW -> ::TPowderSnow
                Material.PURPLE_WOOL -> ::TPurpleWool
                Material.RED_BED -> ::TRedBed
                Material.RED_CARPET -> ::TRedCarpet
                Material.REPEATER -> ::TRepeater
                Material.RESPAWN_ANCHOR -> ::TRespawnAnchor
                Material.SCULK -> ::TSculk
                Material.SEA_LANTERN -> ::TSeaLantern
                Material.SOUL_SAND -> ::TSoulSand
                Material.SPONGE -> ::TSponge
                Material.SPRUCE_LEAVES -> ::TSpruceLeaves
                Material.SPRUCE_TRAPDOOR -> ::TSpruceTrapdoor
                Material.STONECUTTER -> ::TStonecutter
                Material.VERDANT_FROGLIGHT -> ::TVerdantFroglight
                Material.WATER -> ::TWater
                Material.WHITE_WOOL -> ::TWhiteWool
                else -> { event.isCancelled = true; null }
            }
            is PersonGame -> when (block.type) {
                Material.LIGHT_BLUE_CARPET -> ::PTLightBlueCarpet
                Material.DAYLIGHT_DETECTOR -> ::PTDaylightDetector
                Material.RED_WOOL -> ::PTRedWool
                Material.BEE_NEST -> ::PTBeeNest
                Material.CHERRY_PRESSURE_PLATE -> ::PTCherryPressurePlate
                Material.LEVER -> ::PTLever
                Material.DIAMOND_BLOCK -> ::PTDiamondBlock
                Material.PINK_CARPET -> ::PTPinkCarpet
                Material.BIRCH_LOG -> ::PTBirchLog
                Material.CAKE -> ::PTCake
                Material.BAMBOO_BUTTON -> ::PTBambooButton
                Material.ORANGE_WOOL -> ::PTOrangeWool
                Material.BRAIN_CORAL -> ::PTBrainCoral
                Material.GLOWSTONE -> ::PTGlowstone
                Material.RED_STAINED_GLASS -> ::PTRedStainedGlass
                Material.CHERRY_BUTTON -> ::PTCherryButton
                Material.BLUE_CONCRETE -> ::PTBlueConcrete
                Material.GREEN_CARPET -> ::PTGreenCarpet
                Material.IRON_TRAPDOOR -> ::PTIronTrapdoor
                Material.BARREL -> ::PTBarrel
                Material.GRAY_WOOL -> ::PTGrayWool
                Material.BROWN_STAINED_GLASS -> ::PTBrownStainedGlass
                Material.GOLD_BLOCK -> ::PTGoldBlock
                Material.VERDANT_FROGLIGHT -> ::PTVerdantFroglight
                Material.FLETCHING_TABLE -> ::PTFletchingTable
                Material.HONEY_BLOCK -> ::PTHoneyBlock
                Material.WHITE_STAINED_GLASS -> ::PTWhiteStainedGlass
                Material.BLUE_ICE -> ::PTBlueIce
                Material.BLUE_STAINED_GLASS -> ::PTBlueStainedGlass
                Material.POINTED_DRIPSTONE -> ::PTDripstone
                Material.NOTE_BLOCK -> ::PTNoteBlock
                Material.BLACK_CARPET -> ::PTBlackCarpet
                Material.OAK_FENCE_GATE -> ::PTFenceGate
                Material.AMETHYST_BLOCK -> ::PTAmethystBlock
                Material.WHITE_WOOL -> ::PTWhiteWool
                Material.STONECUTTER -> ::PTStonecutter
                Material.SEA_LANTERN -> ::PTSeaLantern
                Material.PURPLE_WOOL -> ::PTPurpleWool
                else -> { event.isCancelled = true; null }
            }
            else -> { event.isCancelled = true; null }
        }?.invoke(block, cord, player), event)
    }

    private fun handleStructure(event: BlockPlaceEvent, block: Block, player: NeoPlayer) {
        val structure = StructureManager.match(player.game.arena) ?: return
        val placed = PlacedStructure(structure, block)
        val cord = placed.lastBlock.location.toCord()

        player.game.turn(when (player.game) {
            is HectusGame -> when (structure.name) {
                "default.iron_bar_jail" -> ::TIronBarJail
                "default.oak_door_turtling" -> ::TOakDoorTurtling
                "default.glass_wall.default" -> ::TGlassWall
                "default.glass_wall.blue" -> ::TBlueGlassWall
                "default.glass_wall.orange" -> ::TOrangeGlassWall
                "default.glass_wall.pink" -> ::TPinkGlassWall
                "default.glass_wall.red" -> ::TRedGlassWall
                else -> null
            }
            is DefaultGame -> when (structure.name) {
                "default.daylight_sensor_line" -> ::TDaylightSensorLine
                "default.pumpkin_wall" -> ::TPumpkinWall
                "default.redstone_wall" -> ::TRedstoneWall
                "default.glass_wall.green" -> ::TGreenGlassWall
                "default.glass_wall.white" -> ::TWhiteGlassWall
                "default.warp.amethyst" -> ::TAmethystWarp
                "default.warp.cliff" -> ::TCliffWarp
                "default.warp.desert" -> ::TDesertWarp
                "default.warp.end" -> ::TEndWarp
                "default.warp.frozen" -> ::TFrozenWarp
                "default.warp.meadow" -> ::TMeadowWarp
                "default.warp.mushroom" -> ::TMushroomWarp
                "default.warp.nerd" -> ::TNerdWarp
                "default.warp.nether" -> ::TNetherWarp
                "default.warp.ocean" -> ::TOceanWarp
                "default.warp.redstone" -> ::TRedstoneWarp
                "default.warp.sun" -> ::TSunWarp
                "default.warp.void" -> ::TVoidWarp
                "default.warp.wood" -> ::TWoodWarp
                else -> null
            }
            is PersonGame -> when (structure.name) {
                "person.candle_circle" -> ::PTCandleCircle
                "person.pumpkin_wall" -> ::PTPumpkinWall
                "person.stone_wall" -> ::PTStoneWall
                "person.torch_circle" -> ::PTTorchCircle
                "person.turtling" -> ::PTTurtling
                "person.wood_wall" -> ::PTWoodWall
                "person.warp.amethyst" -> ::PTAmethystWarp
                "person.warp.fire" -> ::PTFireWarp
                "person.warp.ice" -> ::PTIceWarp
                "person.warp.snow" -> ::PTSnowWarp
                "person.warp.villager" -> ::PTVillagerWarp
                "person.warp.void" -> ::PTVoidWarp
                else -> null
            }
            else -> null
        }?.invoke(placed, cord, player), event)
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

                    fun <T : LivingEntity> turn(c: Class<T>, a: (T, Cord, NeoPlayer) -> MobTurn<T>) {
                        player.game.turn(a(loc.world.spawn(loc, c), loc.toCord(), player), event)
                    }

                    when (event.material) {
                        Material.AXOLOTL_SPAWN_EGG -> turn(Axolotl::class.java, ::TAxolotl)
                        Material.BEE_SPAWN_EGG -> turn(Bee::class.java, ::TBee)
                        Material.BLAZE_SPAWN_EGG -> turn(Blaze::class.java, ::TBlaze)
                        Material.EVOKER_SPAWN_EGG -> turn(Evoker::class.java, ::TEvoker)
                        Material.PHANTOM_SPAWN_EGG -> turn(Phantom::class.java, ::TPhantom)
                        Material.PIGLIN_SPAWN_EGG -> turn(Piglin::class.java, ::TPiglin)
                        Material.POLAR_BEAR_SPAWN_EGG -> turn(PolarBear::class.java, ::TPolarBear)
                        Material.PUFFERFISH_SPAWN_EGG -> turn(PufferFish::class.java, ::TPufferfish)
                        Material.SHEEP_SPAWN_EGG -> turn(Sheep::class.java, ::TSheep)
                        else -> {}
                    }
                    event.isCancelled = true
                } else if (event.clickedBlock != null && blockData is NoteBlock) {
                    player.game.turn(TNoteBlock(blockData, event.clickedBlock!!.location.toCord(), player), event)
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
                player.game.turn(PTPainting(entity, entity.location.toCord(), player), event)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityPlace(event: EntityPlaceEvent) {
        val entity = event.entity
        if (entity is Boat) {
            val player = GameManager.player(event.player!!, true)
            if (unusable(event.player!!, player, true, event, "You cannot place boats right now!")) return

            if (player!!.game is DefaultGame)
                player.game.turn(TBoat(entity, entity.location.toCord(), player), event)
        } else if (entity is ArmorStand) {
            val player = GameManager.player(event.player!!, true)
            if (unusable(event.player!!, player, true, event, "You cannot place armor stands right now!")) return

            if (player!!.game is PersonGame)
                player.game.turn(PTArmorStand(entity, entity.location.toCord(), player), event)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
        if (event.item.type == Material.CHORUS_FRUIT) {
            val player = GameManager.player(event.player, true)
            if (unusable(event.player, player, true, event, "You cannot consume chorus fruits right now!")) return

            event.isCancelled = true // Prevent vanilla teleportation!

            if (player!!.game is DefaultGame)
                player.game.turn(TChorusFruit(event.item, event.player.location.toCord(), player), event)
        } else if (event.item.type == Material.SUSPICIOUS_STEW) {
            val player = GameManager.player(event.player, true)
            if (unusable(event.player, player, true, event, "You cannot eat enchanted suspicious stew right now!")) return

            if (player!!.game is PersonGame)
                player.game.turn(PTSuspiciousStew(event.item, event.player.location.toCord(), player), event)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityTransform(event: EntityTransformEvent) {
        if (event.transformReason == EntityTransformEvent.TransformReason.PIGLIN_ZOMBIFIED) {
            val game = GameManager[event.entity.scoreboardTags]
            if (game is DefaultGame) {
                val player = game.currentPlayer()
                TPiglin(null, null, player).buffs().forEach { it(player) }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerFlowerPotManipulate(event: PlayerFlowerPotManipulateEvent) {
        if (event.isPlacing) {
            val player = GameManager.player(event.player, true)
            if (unusable(event.player, player, true, event, "You cannot place flowers in flower pots right now!")) return

            val block = event.flowerpot
            val cord = block.location.toCord()

            player!!.game.turn(when (event.item.type) {
                Material.ALLIUM -> ::TAllium
                Material.AZURE_BLUET -> ::TAzureBluet
                Material.BLUE_ORCHID -> ::TBlueOrchid
                Material.CORNFLOWER -> ::TCornflower
                Material.ORANGE_TULIP -> ::TOrangeTulip
                Material.OXEYE_DAISY -> ::TOxeyeDaisy
                Material.PINK_TULIP -> ::TPinkTulip
                Material.POPPY -> ::TPoppy
                Material.RED_TULIP -> ::TRedTulip
                Material.SUNFLOWER -> ::TSunflower
                Material.WHITE_TULIP -> ::TWhiteTulip
                Material.WITHER_ROSE -> ::TWitherRose
                else -> { event.isCancelled = true; null }
            }?.invoke(block, cord, player), event)
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
                        PotionType.WATER -> player.game.turn(TSplashWaterBottle(event.potion, event.potion.location.toCord(), player), event)
                        PotionType.STRONG_LEAPING -> player.game.turn(TSplashJumpBoostPotion(event.potion, event.potion.location.toCord(), player), event)
                        else -> {}
                    }
                } else if (meta.hasCustomEffect(PotionEffectType.LEVITATION)) {
                    player.game.turn(TSplashLevitationPotion(event.potion, event.potion.location.toCord(), player), event)
                }
            } else if (player.game is PersonGame) {
                player.game.turn(PTSplashPotion(event.potion, event.potion.location.toCord(), player), event)
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
                        player.game.turn(TSnowball(entity, entity.location.toCord(), player), event)
                    } else if (event.entity is EnderPearl) {
                        player.game.turn(TEnderPearl(entity, entity.location.toCord(), player), event)
                    }
                }
                is PersonGame -> {
                    if (entity is Snowball)
                        player.game.turn(PTSnowball(entity, entity.location.toCord(), player), event)
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
            player.game.turn(TIronShovel(event.itemDrop.itemStack, event.itemDrop.location.toCord(), player), event)
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
