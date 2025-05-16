package net.hectus.neobb.game.mode

import net.hectus.neobb.NeoBB
import net.hectus.neobb.game.Game
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.game.util.GameInfo
import net.hectus.neobb.modes.lore.CardItemLoreBuilder
import net.hectus.neobb.modes.shop.RandomizedShop
import net.hectus.neobb.modes.turn.Turn
import net.hectus.neobb.modes.turn.card_game.*
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.component
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
        loreBuilder = CardItemLoreBuilder::class,
        turns = listOf(
            CTChest::class, CTDaylightDetector::class, CTFlowerPot::class, CTJackOLantern::class, CTOakDoor::class,
            CTOakFenceGate::class, CTPointedDripstone::class, CTRedstoneLamp::class, CTTorch::class,
            CTWaxedExposedCutCopperStairs::class
        ),
    )

    override fun postTurn(turn: Turn<*>, skipped: Boolean) {
        turn.player!!.inventory.fillInRandomly()
    }

    override fun scoreboard(player: NeoPlayer): List<Component>? {
        val locale = player.locale()
        return try {
            listOf(
                MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"),
                locale.component("scoreboard.turning", color = Colors.BLUE)
                    .append(Component.text(if (currentPlayer() === player) "You" else currentPlayer().name(), Colors.RESET)),
                locale.component("scoreboard.time", color = Colors.BLUE)
                    .append(Component.text(player.game.timeLeft.preciselyFormatted, Colors.RESET)),
                locale.component("scoreboard.health").color(Colors.BLUE)
                    .append(Component.text(player.health, Colors.RESET)),
                Component.empty(),
                Component.text("NeoBB-" + NeoBB.VERSION + " (d" + Integer.toHexString(LocalDateTime.now().dayOfYear) + "h" + LocalDateTime.now().hour + ")", Colors.EXTRA),
                Component.text("mc.hectus.net", Colors.LINK)
            )
        } catch (e: Exception) {
            listOf(MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"))
        }
    }

    // Always return true, basically disabling any usage rules:
    override fun allows(turn: Turn<*>): Boolean = true
}
