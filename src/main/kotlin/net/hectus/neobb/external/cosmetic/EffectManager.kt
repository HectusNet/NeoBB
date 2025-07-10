package net.hectus.neobb.external.cosmetic

import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.default_game.BlockTurn
import net.hectus.neobb.modes.turn.default_game.MobTurn
import net.hectus.neobb.modes.turn.default_game.StructureTurn
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Constants
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Display
import org.bukkit.entity.Entity
import org.bukkit.util.Transformation
import org.joml.AxisAngle4f
import org.joml.Vector3f

class EffectManager {
    private var highlight: Entity? = null

    fun applyEffects(turn: Turn<*>) {
        val player = turn.player ?: return

        player.game.world.playSound(turn.location(), player.databaseInfo.placeSound, 1.0f, 1.0f)
        spawnParticle(turn.location(), turn.player)

        when (turn) {
            is BlockTurn -> highlightBlock(turn.data!!, turn.player)
            is StructureTurn -> highlightBlock(turn.data!!.lastBlock, turn.player)
            is MobTurn<*> -> applyHighlight(turn.data!!, turn.player)
        }
    }

    fun spawnParticle(location: Location, player: NeoPlayer) {
        player.databaseInfo.placeParticle.spawn(location)
    }

    fun highlightBlock(block: Block, player: NeoPlayer) {
        val display = block.world.spawn(block.location, BlockDisplay::class.java)
        display.block = block.blockData.clone()
        display.transformation = Transformation(
            Vector3f(0.0f),
            AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f),
            Vector3f(Constants.HIGHLIGHT_SCALE),
            AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f)
        )
        display.brightness = Display.Brightness(block.lightFromBlocks.toInt(), block.lightFromSky.toInt())

        applyHighlight(display, player)
    }

    fun applyHighlight(entity: Entity, player: NeoPlayer) {
        clearHighlight()
        entity.isGlowing = true
        player.team.addEntity(entity)
        highlight = entity
    }

    fun clearHighlight() {
        highlight?.remove()
        highlight = null
    }
}
