package net.hectus.neobb.game.mode

import net.hectus.neobb.NeoBB
import net.hectus.neobb.external.rating.Rank
import net.hectus.neobb.external.rating.Rank.Companion.toRankTranslations
import net.hectus.neobb.game.Game
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.game.util.GameInfo
import net.hectus.neobb.modes.lore.PersonItemLoreBuilder
import net.hectus.neobb.modes.shop.PersonShop
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.person_game.block.*
import net.hectus.neobb.modes.turn.person_game.categorization.*
import net.hectus.neobb.modes.turn.person_game.item.PTSuspiciousStew
import net.hectus.neobb.modes.turn.person_game.other.PTArmorStand
import net.hectus.neobb.modes.turn.person_game.other.PTPainting
import net.hectus.neobb.modes.turn.person_game.structure.*
import net.hectus.neobb.modes.turn.person_game.throwable.PTSnowball
import net.hectus.neobb.modes.turn.person_game.throwable.PTSplashPotion
import net.hectus.neobb.modes.turn.person_game.warp.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.component
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
            buffs.forEach { it.apply(turn.player!!) }
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

    override fun scoreboard(player: NeoPlayer): List<Component>? {
        val locale = player.locale()
        return try {
            listOf(
                MiniMessage.miniMessage().deserialize("<bold><#328825>Block <#37BF1F>Battles <reset><#9D9D9D>Alpha " + NeoBB.VERSION),
                Component.empty(),
                locale.component("scoreboard.stats", color = Colors.PERSON_2, decoration = TextDecoration.BOLD),
                Component.text("💎", Colors.PERSON_4).append(locale.component("scoreboard.rank", color = Colors.PERSON_0))
                    .append(Rank.ofElo(player.databaseInfo.elo).toRankTranslations(locale)),
                Component.text("⚔", Colors.PERSON_4).append(locale.component("scoreboard.elo", color = Colors.PERSON_0))
                    .append(Component.text(player.databaseInfo.elo.toInt(), Colors.PERSON_3)),
                Component.empty(),
                Component.empty(),
                Component.text("mc", Colors.PERSON_1).append(Component.text(".hectus", Colors.PERSON_2)).append(Component.text(".net", Colors.PERSON_3))
            )
        } catch (e: Exception) {
            listOf(MiniMessage.miniMessage().deserialize("<bold><#328825>Block <#37BF1F>Battles <reset><#9D9D9D>Alpha " + NeoBB.VERSION))
        }
    }
}
