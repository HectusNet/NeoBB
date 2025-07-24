package net.hectus.neobb.game.mode

import com.marcpg.libpg.display.BlankScoreboardEntry
import com.marcpg.libpg.display.SimpleScoreboard
import com.marcpg.libpg.display.StaticScoreboardEntry
import com.marcpg.libpg.display.ValueScoreboardEntry
import com.marcpg.libpg.util.component
import net.hectus.neobb.NeoBB
import net.hectus.neobb.external.rating.Rank
import net.hectus.neobb.external.rating.Rank.Companion.toRankTranslations
import net.hectus.neobb.game.Game
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.game.util.GameInfo
import net.hectus.neobb.modes.lore.PersonItemLoreBuilder
import net.hectus.neobb.modes.shop.PersonShop
import net.hectus.neobb.modes.turn.TurnExec
import net.hectus.neobb.modes.turn.person_game.ArmorCategory
import net.hectus.neobb.modes.turn.person_game.CounterCategory
import net.hectus.neobb.modes.turn.person_game.DefensiveCategory
import net.hectus.neobb.modes.turn.person_game.WinConCategory
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.World
import org.bukkit.entity.Player

/**
 * Source: [Semi-Official Google Document](https://docs.google.com/document/d/1SOp-fDTZqx2l3XJoT0zqf4CDuTNCffU2pTejKrzEGO4)
 */
class PersonGame(world: World, bukkitPlayers: List<Player>, difficulty: GameDifficulty = GameDifficulty.NORMAL) : Game(world, bukkitPlayers, difficulty) {
    override val info: GameInfo = GameInfo(
        namespace = "person",
        startingHealth = 10.0,
        turnTimer = 10,
        shop = PersonShop::class,
        loreBuilder = PersonItemLoreBuilder::class,
    )

    override val scoreboard: ((NeoPlayer) -> SimpleScoreboard)? = { p -> SimpleScoreboard(p, 5, MiniMessage.miniMessage().deserialize("<bold><#328825>Block <#37BF1F>Battles <reset><#9D9D9D>Alpha " + NeoBB.VERSION),
        BlankScoreboardEntry(),
        StaticScoreboardEntry(p.locale().component("scoreboard.stats", color = Colors.PERSON_2, decoration = TextDecoration.BOLD)),
        ValueScoreboardEntry(Component.text("💎", Colors.PERSON_4).append(p.locale().component("scoreboard.rank", color = Colors.PERSON_0))) { Rank.ofElo(p.databaseInfo.elo).toRankTranslations(p.locale()) },
        ValueScoreboardEntry(Component.text("⚔", Colors.PERSON_4).append(p.locale().component("scoreboard.elo", color = Colors.PERSON_0))) { Component.text(p.databaseInfo.elo.toInt()) },
        BlankScoreboardEntry(),
        BlankScoreboardEntry(),
        StaticScoreboardEntry(Component.text("mc", Colors.PERSON_1).append(Component.text(".hectus", Colors.PERSON_2)).append(Component.text(".net", Colors.PERSON_3))),
    ) }

    override fun preTurn(exec: TurnExec<*>): Boolean {
        if (exec.turn is WinConCategory && exec.game.hasModifier(Modifiers.Game.Person.NO_WIN_CONS))
            return true

        return super.preTurn(exec)
    }

    override fun executeTurn(exec: TurnExec<*>): Boolean {
        if (exec.turn is ArmorCategory) {
            exec.player.addArmor(exec.turn.armor().toDouble())
        }

        val buffs = exec.turn.buffs
        if (buffs.isNotEmpty()) {
            info("Applying ${buffs.size} buffs from turn.")
            buffs.forEach { it(exec.player) }
        }

        if (exec.turn is CounterCategory) {
            if (exec.turn.counterLogic(exec)) {
                info("Misplaced/misused counter.")
                return false
            } else {
                info("Giving back health, due to counter.")
            }
        }

        if (exec.turn is DefensiveCategory) {
            info("{}: Applying defense from turn.")
            exec.turn.applyDefense(exec)
        }

        return true
    }
}
