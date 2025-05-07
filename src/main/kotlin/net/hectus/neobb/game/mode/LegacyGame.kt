package net.hectus.neobb.game.mode

import com.marcpg.libpg.data.time.Time
import net.hectus.neobb.game.HectusGame
import net.hectus.neobb.game.util.GameDifficulty
import net.hectus.neobb.game.util.GameInfo
import net.hectus.neobb.lore.LegacyItemLoreBuilder
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.shop.DefaultShop
import net.hectus.neobb.turn.Turn
import net.hectus.neobb.turn.default_game.TTimeLimit
import net.hectus.neobb.turn.default_game.attribute.clazz.RedstoneClazz
import net.hectus.neobb.turn.default_game.attribute.function.AttackFunction
import net.hectus.neobb.turn.default_game.block.*
import net.hectus.neobb.turn.default_game.flower.*
import net.hectus.neobb.turn.default_game.item.TChorusFruit
import net.hectus.neobb.turn.default_game.item.TIronShovel
import net.hectus.neobb.turn.default_game.mob.*
import net.hectus.neobb.turn.default_game.other.TBoat
import net.hectus.neobb.turn.default_game.structure.TIronBarJail
import net.hectus.neobb.turn.default_game.structure.TOakDoorTurtling
import net.hectus.neobb.turn.default_game.structure.glass_wall.TBlueGlassWall
import net.hectus.neobb.turn.default_game.structure.glass_wall.TOrangeGlassWall
import net.hectus.neobb.turn.default_game.structure.glass_wall.TPinkGlassWall
import net.hectus.neobb.turn.default_game.structure.glass_wall.TRedGlassWall
import net.hectus.neobb.turn.default_game.throwable.*
import net.hectus.neobb.turn.default_game.warp.WarpTurn
import net.hectus.neobb.turn.legacy_game.block.LTPurpleWool
import net.hectus.neobb.turn.legacy_game.block.LTSeaPickle
import net.hectus.neobb.turn.legacy_game.block.LTTnt
import net.hectus.neobb.turn.legacy_game.item.LTDinnerboneTag
import net.hectus.neobb.turn.legacy_game.item.LTEnchantedGoldenApple
import net.hectus.neobb.turn.legacy_game.item.LTLightningTrident
import net.hectus.neobb.turn.legacy_game.other.LTNoteBlock
import net.hectus.neobb.turn.legacy_game.structure.LTDaylightSensorStrip
import net.hectus.neobb.turn.legacy_game.structure.LTNetherPortal
import net.hectus.neobb.turn.legacy_game.structure.LTPumpkinWall
import net.hectus.neobb.turn.legacy_game.structure.LTRedstoneBlockWall
import net.hectus.neobb.turn.legacy_game.structure.glass_wall.LTGlassWall
import net.hectus.neobb.turn.legacy_game.structure.glass_wall.LTLightBlueGlassWall
import net.hectus.neobb.turn.legacy_game.structure.glass_wall.LTLimeGlassWall
import net.hectus.neobb.turn.legacy_game.warp.*
import net.hectus.neobb.util.Colors
import net.hectus.neobb.util.Modifiers
import net.hectus.neobb.util.component
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.entity.Player

/**
 * Source: [Official Google Document](https://docs.google.com/document/d/1bsNhDuQwWYMZ2S2rbVMgGOIqGsXPzPCjC8K-hDcorZU)
 */
class LegacyGame(world: World, bukkitPlayers: List<Player>, difficulty: GameDifficulty = GameDifficulty.NORMAL) : HectusGame(world, bukkitPlayers, difficulty) {
    override val info: GameInfo = GameInfo(
        namespace = "legacy",
        coins = 64,
        totalTime = Time(15, Time.Unit.MINUTES),
        turnTimer = 15,
        shop = DefaultShop::class,
        loreBuilder = LegacyItemLoreBuilder::class,
        turns = listOf(
            LTAetherWarp::class, LTAmethystWarp::class, LTBookWarp::class, LTCliffWarp::class, LTDaylightSensorStrip::class,
            LTDesertWarp::class, LTDinnerboneTag::class, LTEnchantedGoldenApple::class, LTEndWarp::class, LTGlassWall::class,
            LTHeavenWarp::class, LTHellWarp::class, LTIceWarp::class, LTLightBlueGlassWall::class, LTLightningTrident::class,
            LTLimeGlassWall::class, LTMushroomWarp::class, LTNetherPortal::class, LTNetherWarp::class, LTNoteBlock::class,
            LTPumpkinWall::class, LTPurpleWool::class, LTRedstoneBlockWall::class, LTRedstoneWarp::class, LTSeaPickle::class,
            LTSnowWarp::class, LTSunWarp::class, LTTnt::class, LTUnderwaterWarp::class, LTVoidWarp::class, LTWoodWarp::class,
            TAllium::class, TAxolotl::class, TAzureBluet::class, TBee::class, TBeeNest::class, TBlackWool::class,
            TBlaze::class, TBlueBed::class, TBlueGlassWall::class, TBlueIce::class, TBlueOrchid::class, TBoat::class,
            TBrainCoralBlock::class, TCampfire::class, TCauldron::class, TChorusFruit::class, TComposter::class,
            TCornflower::class, TCyanCarpet::class, TDirt::class, TDragonHead::class, TDriedKelpBlock::class,
            TEnderPearl::class, TEvoker::class, TFenceGate::class, TFire::class, TFireCoral::class, TFireCoralFan::class,
            TFlowerPot::class, TGoldBlock::class, TGreenBed::class, TGreenCarpet::class, TGreenWool::class, THayBlock::class,
            THoneyBlock::class, THornCoral::class, TIronBarJail::class, TIronShovel::class, TIronTrapdoor::class,
            TLava::class, TLever::class, TLightBlueWool::class, TLightningRod::class, TMagentaGlazedTerracotta::class,
            TMagmaBlock::class, TMangroveRoots::class, TNetherrack::class, TOakDoorTurtling::class, TOakStairs::class,
            TOrangeGlassWall::class, TOrangeTulip::class, TOrangeWool::class, TOxeyeDaisy::class, TPackedIce::class,
            TPhantom::class, TPiglin::class, TPinkBed::class, TPinkGlassWall::class, TPinkTulip::class, TPiston::class,
            TPolarBear::class, TPoppy::class, TPowderSnow::class, TPufferfish::class, TRedBed::class, TRedCarpet::class,
            TRedGlassWall::class, TRedTulip::class, TRepeater::class, TRespawnAnchor::class, TSculk::class,
            TSeaLantern::class, TSheep::class, TSnowball::class, TSoulSand::class, TSplashJumpBoostPotion::class,
            TSplashLevitationPotion::class, TSplashWaterBottle::class, TSponge::class, TSpruceLeaves::class,
            TSpruceTrapdoor::class, TStonecutter::class, TSunflower::class, TVerdantFroglight::class, TWater::class,
            TWhiteTulip::class, TWhiteWool::class, TWitherRose::class
        ),
    )

    override fun preTurn(turn: Turn<*>): Boolean {
        if (turn is TTimeLimit) {
            eliminate(turn.player!!)
            return true
        }

        if (!difficulty.completeRules && history.isEmpty() && (turn is AttackFunction || turn is WarpTurn)) {
            turn.player!!.sendMessage(Component.text("You cannot use attacks or warps as the first turn.", Colors.NEGATIVE))
            turn.player.playSound(Sound.ENTITY_VILLAGER_NO)
            return true
        }

        return false
    }

    override fun executeTurn(turn: Turn<*>): Boolean {
        if (warp is LTRedstoneWarp && turn is RedstoneClazz)
            turn.player!!.addLuck(5)
        return super.executeTurn(turn)
    }

    override fun scoreboard(player: NeoPlayer): List<Component>? {
        val locale = player.locale()
        return try {
            listOf(
                MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"),
                Component.empty(),
                Component.text(player.name(), Colors.EXTRA),
                Component.empty(),
                locale.component("scoreboard.turning", color = Colors.EXTRA)
                    .append(Component.text(currentPlayer().name())),
                Component.empty(),
                locale.component("scoreboard.luck", color = Colors.EXTRA)
                    .append(Component.text(player.luck, Colors.RESET)),
                Component.empty(),
                locale.component("scoreboard.attacked", color = Colors.EXTRA)
                    .append(locale.component("shop.filter." + (if (player.hasModifier(Modifiers.Player.Default.ATTACKED)) "yes" else "no")).color(Colors.RESET)),
                locale.component("scoreboard.defended", color = Colors.EXTRA)
                    .append(locale.component("shop.filter." + (if (player.hasModifier(Modifiers.Player.Default.DEFENDED)) "yes" else "no")).color(Colors.RESET)),
                locale.component("scoreboard.frozen", color = Colors.EXTRA)
                    .append(locale.component("shop.filter." + (if (player.hasModifier(Modifiers.Player.NO_MOVE)) "yes" else "no")).color(Colors.RESET))
            )
        } catch (e: Exception) {
            listOf(MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"))
        }
    }
}