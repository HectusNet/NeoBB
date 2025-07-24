package net.hectus.neobb.game.mode

import com.marcpg.libpg.display.*
import com.marcpg.libpg.lang.string
import com.marcpg.libpg.util.component
import net.hectus.neobb.NeoBB
import net.hectus.neobb.game.Game
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.game.util.GameInfo
import net.hectus.neobb.modes.lore.CardItemLoreBuilder
import net.hectus.neobb.modes.shop.RandomizedShop
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.World
import org.bukkit.entity.Player
import java.time.LocalDateTime

/**
 * Source: [Source Code on GitHub](https://github.com/TiagoFar78/BlockBattles)
 *
 * Originally named HereGame, because it was based on HereStudio, but renamed to CardGame.
 */
class CardGame(world: World, bukkitPlayers: List<Player>, difficulty: GameDifficulty = GameDifficulty.NORMAL) : Game(world, bukkitPlayers, difficulty) {
    override val info: GameInfo = GameInfo(
        namespace = "card",
        hasStructures = false,
        startingHealth = 40.0,
        coins = 0,
        deckSize = 5,
        turnTimer = 15,
        shop = RandomizedShop::class,
        loreBuilder = CardItemLoreBuilder::class
    )

    override val scoreboard: ((NeoPlayer) -> SimpleScoreboard)? = { p -> SimpleScoreboard(p, 5, MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"),
        ValueScoreboardEntry(p.locale().component("scoreboard.turning", color = Colors.BLUE)) { component(if (currentPlayer() === p) p.locale().string("scoreboard.turning.you") else currentPlayer().name()) },
        ValueScoreboardEntry(p.locale().component("scoreboard.time", color = Colors.BLUE)) { component(p.game.timeLeft.preciselyFormatted) },
        ValueScoreboardEntry(p.locale().component("scoreboard.health", color = Colors.BLUE)) { Component.text(p.health) },
        BlankScoreboardEntry(),
        SimpleScoreboardEntry { component("NeoBB-" + NeoBB.VERSION + " (d" + Integer.toHexString(LocalDateTime.now().dayOfYear) + "h" + LocalDateTime.now().hour + ")", Colors.EXTRA) },
        StaticScoreboardEntry(component("mc.hectus.net", Colors.LINK)),
    ) }

    override fun postTurn(exec: TurnExec<*>, skipped: Boolean) {
        exec.player.inventory.fillInRandomly()
    }

    // Always return true, basically disabling any usage rules:
    override fun allows(turn: Turn<*>): Boolean = true
}
