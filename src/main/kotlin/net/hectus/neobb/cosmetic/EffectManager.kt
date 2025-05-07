package net.hectus.neobb.cosmetic

import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.block.BlockTurn
import net.hectus.neobb.turn.default_game.mob.MobTurn
import net.hectus.neobb.turn.default_game.structure.StructureTurn
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Shulker
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class EffectManager {
    private var highlight: LivingEntity? = null

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

    @Suppress("removal")
    fun highlightBlock(block: Block, outlineColor: NamedTextColor) {
        if (block.type.name.contains("GLASS") || block.type.isTransparent) return // TODO: Improve this check!

        block.world.spawn(block.location, Shulker::class.java) { shulker ->
            shulker.setAI(false)
            shulker.isInvisible = true
            shulker.isInvulnerable = true
            applyHighlight(shulker, outlineColor)
        }
    }

    fun applyHighlight(entity: LivingEntity, outlineColor: NamedTextColor) {
        clearHighlight()
        highlight = entity

        entity.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, -1, 0, false, false))

        val team = Bukkit.getScoreboardManager().mainScoreboard.registerNewTeam("highlight-${entity.uniqueId.mostSignificantBits}")
        team.color(outlineColor)
        team.addEntity(entity)
    }

    fun clearHighlight() {
        highlight?.let { when (it) {
            is Shulker -> it.remove()
            else -> it.removePotionEffect(PotionEffectType.GLOWING)
        } }
    }
}
