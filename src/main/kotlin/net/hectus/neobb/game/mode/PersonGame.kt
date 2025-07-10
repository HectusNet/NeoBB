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
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.person_game.*
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
        turns = listOf(
            PTAmethystBlock::class, PTAmethystWarp::class, PTArmorStand::class, PTBambooButton::class, PTBarrel::class,
            PTBeeNest::class, PTBirchLog::class, PTBlackCarpet::class, PTBlueConcrete::class, PTBlueIce::class,
            PTBlueStainedGlass::class, PTBrainCoral::class, PTBrownStainedGlass::class, PTCake::class, PTCandleCircle::class,
            PTCherryButton::class, PTCherryPressurePlate::class, PTDaylightDetector::class, PTDiamondBlock::class,
            PTDripstone::class, PTFenceGate::class, PTFireWarp::class, PTFletchingTable::class, PTGlowstone::class,
            PTGoldBlock::class, PTGrayWool::class, PTGreenCarpet::class, PTHoneyBlock::class, PTIceWarp::class,
            PTIronTrapdoor::class, PTLever::class, PTLightBlueCarpet::class, PTNoteBlock::class, PTOrangeWool::class,
            PTPainting::class, PTPinkCarpet::class, PTPumpkinWall::class, PTPurpleWool::class, PTRedStainedGlass::class,
            PTRedWool::class, PTSeaLantern::class, PTSnowWarp::class, PTSnowball::class, PTSplashPotion::class,
            PTStoneWall::class, PTStonecutter::class, PTSuspiciousStew::class, PTTorchCircle::class, PTTurtling::class,
            PTVerdantFroglight::class, PTVillagerWarp::class, PTVoidWarp::class, PTWhiteStainedGlass::class,
            PTWhiteWool::class, PTWoodWall::class
        ),
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

    override fun preTurn(turn: Turn<*>): Boolean {
        if (turn is WinConCategory && turn.player!!.game.hasModifier(Modifiers.Game.Person.NO_WIN_CONS))
            return true

        return super.preTurn(turn)
    }

    override fun executeTurn(turn: Turn<*>): Boolean {
        if (turn is ArmorCategory) {
            turn.player!!.addArmor(turn.armor().toDouble())
        }

        if (turn is BuffCategory) {
            val buffs = turn.buffs()
            info("Applying ${buffs.size} buffs from turn.")
            buffs.forEach { it(turn.player!!) }
        }

        if (turn is CounterCategory) {
            if (turn.counterLogic(turn)) {
                info("Misplaced/misused counter.")
                return false
            } else {
                info("Giving back health, due to counter.")
            }
        }

        if (turn is DefensiveCategory) {
            info("{}: Applying defense from turn.")
            turn.applyDefense()
        }

        return true
    }
}
