package net.hectus.neobb.cosmetic

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.block.BlockTurn
import net.hectus.neobb.turn.default_game.mob.MobTurn
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.hectus.neobb.util.Constants
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Display
import org.bukkit.entity.Entity
import org.bukkit.scoreboard.Team
import org.bukkit.util.Transformation
import org.joml.AxisAngle4f
import org.joml.Vector3f

class EffectManager {
    private var highlight: Pair<Entity, Team?>? = null

    fun applyEffects(turn: Turn<*>) {
        if (turn.player == null) return

        turn.player.playSound(turn.player.databaseInfo.placeSound)

        val outlineColor = turn.player.databaseInfo.outline
        if (turn is BlockTurn) highlightBlock(turn.data!!, outlineColor)
        if (turn is StructureTurn) highlightBlock(turn.data!!.lastBlock, outlineColor)
        if (turn is MobTurn<*>) applyHighlight(turn.data!!, outlineColor)

        spawnParticle(turn.location(), turn.player)
    }

    fun spawnParticle(location: Location, player: NeoPlayer) {
        player.databaseInfo.placeParticle.spawn(location)
    }

    fun highlightBlock(block: Block, outlineColor: NamedTextColor) {
        clearHighlight()

        val display = block.world.spawn(block.location, BlockDisplay::class.java)
        display.block = block.blockData.clone()
        display.transformation = Transformation(
            Vector3f(0.0f),
            AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f),
            Vector3f(Constants.HIGHLIGHT_SCALE),
            AxisAngle4f(0.0f, 0.0f, 0.0f, 0.0f)
        )
        display.brightness = Display.Brightness(block.lightFromBlocks.toInt(), block.lightFromSky.toInt())

        display.glowColorOverride = Color.fromRGB(outlineColor.value())
        display.isGlowing = true

        highlight = display to null
    }

    fun applyHighlight(entity: Entity, outlineColor: NamedTextColor) {
        clearHighlight()

        val team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("highlight-${entity.uniqueId.mostSignificantBits}")
        team.color(outlineColor)
        team.addEntity(entity)
        highlight = entity to team
    }

    fun clearHighlight() {
        val highlight = highlight ?: return
        when (highlight.first) {
            is BlockDisplay -> highlight.first.remove()
            else -> highlight.first.isGlowing = false
        }
        highlight.second?.unregister()
    }
}
