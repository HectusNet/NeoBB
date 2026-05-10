package net.hectus.neobb.util

import dev.faststats.bukkit.BukkitMetrics
import dev.faststats.core.ErrorTracker
import dev.faststats.core.SimpleMetrics
import dev.faststats.core.data.Metric
import net.hectus.neobb.NeoBB
import net.hectus.neobb.game.GameManager

object Metrics {
    private var errorTracker: ErrorTracker? = null
    private var metrics: BukkitMetrics? = null

    fun start() {
        if (Configuration.disableFastStats) return

        errorTracker = ErrorTracker.contextAware()

        metrics = BukkitMetrics.factory()
            .token("51b3fd2886aecb9814c2bc8925a1e519")

            .addMetric(Metric.number("games_running") { GameManager.games.size })
            .addMetric(Metric.stringArray("games_started") { GameManager.gamesStartedSinceLastFlush.toTypedArray() })
            .addMetric(Metric.number("players_in_games") { GameManager.playersInGamesSinceLastFlush })

            .onFlush {
                GameManager.gamesStartedSinceLastFlush.clear()
                GameManager.playersInGamesSinceLastFlush = 0
            }

            .errorTracker(errorTracker)
            .create(NeoBB.PLUGIN)

        metrics?.ready()
    }

    fun logError(e: Throwable) {
        if (Configuration.disableFastStats) return

        errorTracker?.trackError(e)
    }

    fun forceSubmit() {
        if (Configuration.disableFastStats) return

        (metrics as SimpleMetrics?)?.submit()
    }

    fun shutdown() {
        metrics?.shutdown()
    }
}
