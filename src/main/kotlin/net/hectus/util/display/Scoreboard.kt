package net.hectus.util.display

import io.papermc.paper.scoreboard.numbers.NumberFormat
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.*

open class SimpleScoreboard(
    player: Player, updateInterval: Long,
    protected val title: Component,
    vararg val scores: ScoreboardEntry,
) : SimpleDisplay(player, updateInterval) {
    companion object {
        private val MANAGER = Bukkit.getScoreboardManager()
    }

    private val scoreboard: Scoreboard = if (player.scoreboard == MANAGER.mainScoreboard) MANAGER.newScoreboard else player.scoreboard
    val objective: Objective

    init {
        scoreboard.objectives.forEach { it.unregister() }
        objective = player.scoreboard.getObjective("neobb") ?: player.scoreboard.registerNewObjective("neobb", Criteria.DUMMY, title)
        objective.displaySlot = DisplaySlot.SIDEBAR
    }

    override fun extraStart() {
        player.scoreboard = scoreboard
    }

    override fun setup() {
        for ((index, score) in scores.withIndex()) {
            score.init(index, this)
        }
    }

    override fun update() = scores.forEach { it.update() }

    override fun remove() {
        player.scoreboard = Bukkit.getScoreboardManager().mainScoreboard
    }
}

abstract class ScoreboardEntry {
    lateinit var score: Score

    open fun init(index: Int, scoreboard: SimpleScoreboard) {
        score = scoreboard.objective.getScore("score-$index")
        score.score = scoreboard.scores.size - index
    }

    abstract fun update()
}

open class StaticScoreboardEntry(val text: Component) : ScoreboardEntry() {
    override fun init(index: Int, scoreboard: SimpleScoreboard) {
        super.init(index, scoreboard)
        score.numberFormat(NumberFormat.blank())
        score.customName(Component.empty())
    }

    override fun update() {}
}

open class BlankScoreboardEntry : StaticScoreboardEntry(Component.empty())

open class SimpleScoreboardEntry(val text: () -> Component) : ScoreboardEntry() {
    override fun init(index: Int, scoreboard: SimpleScoreboard) {
        super.init(index, scoreboard)
        score.numberFormat(NumberFormat.blank())
    }

    override fun update() {
        score.customName(text())
    }
}

open class StaticValueScoreboardEntry(title: Component, val value: Component) : StaticScoreboardEntry(title) {
    override fun init(index: Int, scoreboard: SimpleScoreboard) {
        super.init(index, scoreboard)
        score.numberFormat(NumberFormat.fixed(value))
    }
}

open class ValueScoreboardEntry(val title: Component, val value: () -> Component) : ScoreboardEntry() {
    override fun init(index: Int, scoreboard: SimpleScoreboard) {
        super.init(index, scoreboard)
        score.customName(title)
    }

    override fun update() {
        score.numberFormat(NumberFormat.fixed(value))
    }
}
