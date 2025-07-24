package net.hectus.neobb.game.mode

import com.marcpg.libpg.data.time.Time
import com.marcpg.libpg.display.*
import com.marcpg.libpg.lang.string
import com.marcpg.libpg.util.component
import net.hectus.neobb.NeoBB
import net.hectus.neobb.external.rating.Rank
import net.hectus.neobb.external.rating.Rank.Companion.toRankTranslations
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.game.util.GameInfo
import net.hectus.neobb.modes.lore.DefaultItemLoreBuilder
import net.hectus.neobb.modes.shop.DefaultShop
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.World
import org.bukkit.entity.Player
import java.time.LocalDateTime

/**
 * Source: [Official Google Document](https://docs.google.com/document/d/1y05rFNz7QcvB7yoyqvYnPgb925Leq2P-HV-NKBuJUNg)
 */
class DefaultGame(world: World, bukkitPlayers: List<Player>, difficulty: GameDifficulty = GameDifficulty.NORMAL) : HectusGame(world, bukkitPlayers, difficulty) {
    override val info: GameInfo = GameInfo(
        namespace = "default",
        coins = 25,
        totalTime = Time(3, Time.Unit.MINUTES),
        turnTimer = 5,
        shop = DefaultShop::class,
        loreBuilder = DefaultItemLoreBuilder::class,
    )

    override val scoreboard: ((NeoPlayer) -> SimpleScoreboard)? = { p -> SimpleScoreboard(p, 5, MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"),
        ValueScoreboardEntry(p.locale().component("scoreboard.turning", color = Colors.ACCENT)) { Component.text(if (currentPlayer() === p) p.locale().string("scoreboard.turning.you") else currentPlayer().name()) },
        ValueScoreboardEntry(p.locale().component("scoreboard.time", color = Colors.ACCENT)) { Component.text(p.game.timeLeft.preciselyFormatted) },
        ValueScoreboardEntry(p.locale().component("scoreboard.luck", color = Colors.ACCENT)) { Component.text(p.luck) },
        BlankScoreboardEntry(),
        ValueScoreboardEntry(p.locale().component("scoreboard.rank", color = Colors.ACCENT)) { Rank.ofElo(p.databaseInfo.elo).toRankTranslations(p.locale()) },
        ValueScoreboardEntry(p.locale().component("scoreboard.elo", color = Colors.ACCENT)) { Component.text(p.databaseInfo.elo.toInt()) },
        BlankScoreboardEntry(),
        SimpleScoreboardEntry() { Component.text("NeoBB-" + NeoBB.VERSION + " (d" + Integer.toHexString(LocalDateTime.now().dayOfYear) + "h" + LocalDateTime.now().hour + ")", Colors.EXTRA) },
        StaticScoreboardEntry(Component.text("mc.hectus.net", Colors.LINK)),
    ) }

    override val actionBar: ((NeoPlayer) -> SimpleActionBar)? = { p -> SimpleActionBar(p, 1) { if (currentPlayer() === p) {
        if (p.hasModifier(Modifiers.Player.Default.ATTACKED)) {
            if (p.hasModifier(Modifiers.Player.Default.DEFENDED)) {
                p.locale().component("actionbar.defended_attack", color = Colors.NEUTRAL)
            } else {
                p.locale().component("actionbar.attacked", color = Colors.NEGATIVE)
            }
        } else {
            p.locale().component("actionbar.you_turning", color = Colors.POSITIVE)
        }
    } else {
        p.locale().component("actionbar.other_turning", currentPlayer().name(), color = Colors.NEUTRAL)
    } } }
}
