package net.hectus.neobb.game.mode

import com.marcpg.libpg.data.time.Time
import net.hectus.neobb.NeoBB
import net.hectus.neobb.game.HectusGame
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.game.util.GameInfo
import net.hectus.neobb.lore.DefaultItemLoreBuilder
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.shop.DefaultShop
import net.hectus.neobb.turn.default_game.block.*
import net.hectus.neobb.turn.default_game.flower.*
import net.hectus.neobb.turn.default_game.item.TChorusFruit
import net.hectus.neobb.turn.default_game.item.TIronShovel
import net.hectus.neobb.turn.default_game.mob.*
import net.hectus.neobb.turn.default_game.other.TBoat
import net.hectus.neobb.turn.default_game.other.TNoteBlock
import net.hectus.neobb.turn.default_game.structure.*
import net.hectus.neobb.turn.default_game.structure.glass_wall.*
import net.hectus.neobb.turn.default_game.throwable.*
import net.hectus.neobb.turn.default_game.warp.*
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.component
import net.hectus.neobb.util.string
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
        turns = listOf(
            TAllium::class, TAmethystWarp::class, TAxolotl::class, TAzureBluet::class, TBee::class, TBeeNest::class,
            TBlackWool::class, TBlaze::class, TBlueBed::class, TBlueGlassWall::class, TBlueIce::class, TBlueOrchid::class,
            TBoat::class, TBrainCoralBlock::class, TCampfire::class, TCauldron::class, TChorusFruit::class,
            TCliffWarp::class, TComposter::class, TCornflower::class, TCyanCarpet::class, TDaylightSensorLine::class,
            TDesertWarp::class, TDirt::class, TDragonHead::class, TDriedKelpBlock::class, TEndWarp::class,
            TEnderPearl::class, TEvoker::class, TFenceGate::class, TFire::class, TFireCoral::class, TFireCoralFan::class,
            TFlowerPot::class, TFrozenWarp::class, TGlassWall::class, TGoldBlock::class, TGreenBed::class,
            TGreenCarpet::class, TGreenGlassWall::class, TGreenWool::class, THayBlock::class, THoneyBlock::class,
            THornCoral::class, TIronBarJail::class, TIronShovel::class, TIronTrapdoor::class, TLava::class, TLever::class,
            TLightBlueWool::class, TLightningRod::class, TMagentaGlazedTerracotta::class, TMagmaBlock::class,
            TMangroveRoots::class, TMeadowWarp::class, TMushroomWarp::class, TNerdWarp::class, TNetherWarp::class,
            TNetherrack::class, TNoteBlock::class, TOakDoorTurtling::class, TOakStairs::class, TOceanWarp::class,
            TOrangeGlassWall::class, TOrangeTulip::class, TOrangeWool::class, TOxeyeDaisy::class, TPackedIce::class,
            TPhantom::class, TPiglin::class, TPinkBed::class, TPinkGlassWall::class, TPinkTulip::class, TPiston::class,
            TPolarBear::class, TPoppy::class, TPowderSnow::class, TPufferfish::class, TPumpkinWall::class,
            TPurpleWool::class, TRedBed::class, TRedCarpet::class, TRedGlassWall::class, TRedTulip::class,
            TRedstoneWall::class, TRedstoneWarp::class, TRepeater::class, TRespawnAnchor::class, TSculk::class,
            TSeaLantern::class, TSheep::class, TSnowball::class, TSoulSand::class, TSplashJumpBoostPotion::class,
            TSplashLevitationPotion::class, TSplashWaterBottle::class, TSponge::class, TSpruceLeaves::class,
            TSpruceTrapdoor::class, TStonecutter::class, TSunWarp::class, TSunflower::class, TVerdantFroglight::class,
            TVoidWarp::class, TWater::class, TWhiteGlassWall::class, TWhiteTulip::class, TWhiteWool::class,
            TWitherRose::class, TWoodWarp::class
        ),
    )

    override fun onOutOfBounds(player: NeoPlayer) {
        player.damage(2.0)
    }

    override fun scoreboard(player: NeoPlayer): List<Component> {
        val locale = player.locale()
        return try {
            listOf(
                MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"),
                locale.component("scoreboard.turning", color = Colors.ACCENT)
                    .append(Component.text(if (currentPlayer() === player) locale.string("scoreboard.turning.you") else currentPlayer().name(), Colors.RESET)),
                locale.component("scoreboard.time", color = Colors.ACCENT)
                    .append(Component.text(player.game.timeLeft.preciselyFormatted, Colors.RESET)),
                locale.component("scoreboard.luck", color = Colors.ACCENT)
                    .append(Component.text(player.luck, Colors.RESET)),
                Component.empty(),
                Component.text("NeoBB-" + NeoBB.VERSION + " (d" + Integer.toHexString(LocalDateTime.now().dayOfYear) + "h" + LocalDateTime.now().hour + ")", Colors.EXTRA),
                Component.text("mc.hectus.net", Colors.LINK)
            )
        } catch (e: Exception) {
            listOf(MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"))
        }
    }

    override fun actionbar(player: NeoPlayer): Component {
        val locale = player.locale()
        return if (currentPlayer() === player) {
            if (player.hasModifier(Modifiers.Player.Default.ATTACKED)) {
                if (player.hasModifier(Modifiers.Player.Default.DEFENDED)) {
                    locale.component("actionbar.defended_attack", color = Colors.NEUTRAL)
                } else {
                    locale.component("actionbar.attacked", color = Colors.NEGATIVE)
                }
            } else {
                locale.component("actionbar.you_turning", color = Colors.POSITIVE)
            }
        } else {
            locale.component("actionbar.other_turning", currentPlayer().name(), color = Colors.NEUTRAL)
        }
    }
}