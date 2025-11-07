package net.hectus.neobb.event

import com.marcpg.libpg.event.CancellableImpl
import com.marcpg.libpg.util.bukkitRunLater
import com.marcpg.libpg.util.component
import com.marcpg.libpg.util.toCord
import io.papermc.paper.event.player.PlayerFlowerPotManipulateEvent
import net.hectus.neobb.game.GameManager
import net.hectus.neobb.game.mode.CardGame
import net.hectus.neobb.game.mode.DefaultGame
import net.hectus.neobb.game.mode.PersonGame
import net.hectus.neobb.matrix.structure.PlacedStructure
import net.hectus.neobb.matrix.structure.StructureManager
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.card_game.InteractableCardTurn
import net.hectus.neobb.modes.turn.default_game.*
import net.hectus.neobb.modes.turn.person_game.PTArmorStand
import net.hectus.neobb.modes.turn.person_game.PTPainting
import net.hectus.neobb.modes.turn.person_game.PTSuspiciousStew
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
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
import org.bukkit.inventory.meta.PotionMeta

object TurnEvents: Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    fun onBlockPlace(event: BlockPlaceEvent) {
        if (event.block.type == Material.BLACK_STAINED_GLASS_PANE)
            event.isCancelled = true

        val player = GameManager.player(event.player, true)
        if (unusable(event.player, player, true, event, "You cannot place blocks right now!")) return

        val block = event.block

        if (block.type == Material.NOTE_BLOCK)
            return

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
        val type = block.type

        val turn: Turn<Block>? = player.game.info.blockEvents.firstOrNull { it.mainItem.type == type }
        if (turn == null) {
            event.isCancelled = true
            return
        }
        player.game.turn(TurnExec(turn, player, block.location.toCord(), block), event)
    }

    private fun handleStructure(event: BlockPlaceEvent, block: Block, player: NeoPlayer) {
        val match = StructureManager.match(player.game.arena) ?: return
        val placed = PlacedStructure(match, block)

        val turn: StructureTurn? = player.game.info.structureEvents.firstOrNull { it.referenceStructure.name == match.name }
        if (turn == null) {
            event.isCancelled = true
            return
        }
        player.game.turn(TurnExec(turn, player, placed.lastBlock.location.toCord(), placed), event)
    }

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return

        val player = GameManager.player(event.player, true)
        if (unusable(event.player, player, false, event, "You cannot interact right now!")) return

        val location = event.interactionPoint!!
        val cord = location.toCord()
        val clickedBlock = event.clickedBlock

        when (player!!.game) {
            is DefaultGame -> {
                if (player.game.currentPlayer() !== player) {
                    player.sendMessage(component("You cannot interact right now!", Colors.NEGATIVE))
                    event.isCancelled = true
                    return
                }

                if (event.material.name.endsWith("_SPAWN_EGG")) {
                    val entity = enumValueOf<EntityType>(event.material.name.removeSuffix("_SPAWN_EGG"))
                    when (entity) {
                        EntityType.AXOLOTL -> player.game.turn(TurnExec(TAxolotl, player, cord, location.world.spawn(location, Axolotl::class.java, true, null)), null)
                        EntityType.BEE -> player.game.turn(TurnExec(TBee, player, cord, location.world.spawn(location, Bee::class.java, true, null)), null)
                        EntityType.BLAZE -> player.game.turn(TurnExec(TBlaze, player, cord, location.world.spawn(location, Blaze::class.java, true, null)), null)
                        EntityType.EVOKER -> player.game.turn(TurnExec(TEvoker, player, cord, location.world.spawn(location, Evoker::class.java, true, null)), null)
                        EntityType.PHANTOM -> player.game.turn(TurnExec(TPhantom, player, cord, location.world.spawn(location, Phantom::class.java, true, null)), null)
                        EntityType.PIGLIN -> player.game.turn(TurnExec(TPiglin, player, cord, location.world.spawn(location, Piglin::class.java, true, null)), null)
                        EntityType.POLAR_BEAR -> player.game.turn(TurnExec(TPolarBear, player, cord, location.world.spawn(location, PolarBear::class.java, true, null)), null)
                        EntityType.PUFFERFISH -> player.game.turn(TurnExec(TPufferfish, player, cord, location.world.spawn(location, PufferFish::class.java, true, null)), null)
                        EntityType.SHEEP -> player.game.turn(TurnExec(TSheep, player, cord, location.world.spawn(location, Sheep::class.java, true, null)), null)
                        else -> {}
                    }
                    event.isCancelled = true
                } else if (clickedBlock?.blockData is NoteBlock) {
                    player.game.turn(TurnExec(TNoteBlock, player, cord, clickedBlock.blockData as NoteBlock), event)
                }
            }
            is CardGame -> {
                if (player.game.history.isEmpty()) return
                val exec = player.game.history.last()
                if (exec.turn is InteractableCardTurn && (exec.data as Block).location == event.clickedBlock?.location)
                    exec.turn.interact(player)
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onHangingPlace(event: HangingPlaceEvent) {
        val entity = event.entity
        if (entity is Painting) {
            val player = GameManager.player(event.player!!, true)
            if (unusable(event.player!!, player, false, event, "You cannot place paintings right now!")) return

            if (player!!.game is PersonGame)
                player.game.turn(TurnExec(PTPainting, player, entity.location.toCord(), entity), event)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityPlace(event: EntityPlaceEvent) {
        val player = GameManager.player(event.player!!, true)
        if (unusable(event.player!!, player, true, event, "You cannot place entities right now!")) return

        val entity = event.entity
        if (entity is Boat) {
            player!!.game.turn(TurnExec(TBoat, player, entity.location.toCord(), entity), event)
        } else if (entity is ArmorStand) {
            player!!.game.turn(TurnExec(PTArmorStand, player, entity.location.toCord(), entity), event)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerItemConsume(event: PlayerItemConsumeEvent) {
        val player = GameManager.player(event.player, true)
        if (unusable(event.player, player, true, event, "You cannot consume items right now!")) return

        if (event.item.type == Material.CHORUS_FRUIT) {
            event.isCancelled = true // Prevent vanilla teleportation!

            player!!.game.turn(TurnExec(TChorusFruit, player, player.cord(), event.item), event)
        } else if (event.item.type == Material.SUSPICIOUS_STEW) {
            player!!.game.turn(TurnExec(PTSuspiciousStew, player, player.cord(), event.item), event)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onEntityTransform(event: EntityTransformEvent) {
        if (event.transformReason == EntityTransformEvent.TransformReason.PIGLIN_ZOMBIFIED) {
            val game = GameManager[event.entity.scoreboardTags]
            if (game is DefaultGame) {
                val player = game.currentPlayer()
                TPiglin.buffs.forEach { it(player) }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerFlowerPotManipulate(event: PlayerFlowerPotManipulateEvent) {
        if (event.isPlacing) {
            val player = GameManager.player(event.player, true)
            if (unusable(event.player, player, true, event, "You cannot place flowers in flower pots right now!")) return

            val turn: Turn<Block>? = player!!.game.info.flowerEvents.firstOrNull { it.mainItem.type == event.item.type }
            if (turn == null) {
                event.isCancelled = true
                return
            }
            player.game.turn(TurnExec(turn, player, event.flowerpot.location.toCord(), event.flowerpot), event)
        }
    }

    @EventHandler(ignoreCancelled = true)
    fun onPotionSplash(event: PotionSplashEvent) {
        val p = event.potion.shooter
        if (p !is Player) return

        val player = GameManager.player(p, true)
        if (unusable(p, player, true, CancellableImpl(), "You cannot use potions right now!")) return

        val meta = event.potion.potionMeta
        if (!meta.hasBasePotionType())
            return
        val type = meta.basePotionType!!

        val turn: Turn<Projectile>? = player!!.game.info.throwEvents.firstOrNull { it.mainItem.type == Material.SPLASH_POTION && (it.mainItem.itemMeta as PotionMeta).basePotionType == type }
        if (turn == null) {
            event.isCancelled = true
            return
        }
        player.game.turn(TurnExec(turn, player, event.potion.location.toCord(), event.potion), event)
    }

    @EventHandler(ignoreCancelled = true)
    fun onProjectileHit(event: ProjectileHitEvent) {
        val p = event.entity.shooter
        if (p !is Player) return

        val player = GameManager.player(p, true)
        if (unusable(p, player, true, CancellableImpl(), "You cannot throw things right now!")) return

        val turn: Turn<Projectile>? = player!!.game.info.throwEvents.firstOrNull { it.mainItem.type.name == event.entity.type.name }
        if (turn == null) {
            event.isCancelled = true
            return
        }
        player.game.turn(TurnExec(turn, player, event.entity.location.toCord(), event.entity), event)
    }

    @EventHandler(ignoreCancelled = true)
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val player = GameManager.player(event.player, true)
        if (!(player?.game?.started ?: false)) return // Allow dropping in shop before the game started.

        if (unusable(event.player, player, true, event, "You cannot drop items right now!")) return

        if (event.itemDrop.itemStack.type == Material.IRON_SHOVEL)
            player.game.turn(TurnExec(TIronShovel, player, event.itemDrop.location.toCord(), event.itemDrop.itemStack), event)
    }

    private fun unusable(fallbackPlayer: Player, player: NeoPlayer?, requireTurning: Boolean, event: Cancellable, message: String): Boolean {
        if (player == null) {
            if (fallbackPlayer.gameMode != GameMode.CREATIVE) // Allow builders and admins to do this.
                event.isCancelled = true
            return true
        }

        if (!player.game.started || (requireTurning && player.game.currentPlayer() !== player)) {
            player.sendMessage(component(message, Colors.NEGATIVE))
            event.isCancelled = true
            return true
        }
        return false
    }
}
