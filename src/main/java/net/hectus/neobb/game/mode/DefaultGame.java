package net.hectus.neobb.game.mode;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.HectusGame;
import net.hectus.neobb.game.util.Difficulty;
import net.hectus.neobb.game.util.GameInfo;
import net.hectus.neobb.lore.DefaultItemLoreBuilder;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.shop.DefaultShop;
import net.hectus.neobb.turn.default_game.block.*;
import net.hectus.neobb.turn.default_game.flower.*;
import net.hectus.neobb.turn.default_game.item.TChorusFruit;
import net.hectus.neobb.turn.default_game.item.TIronShovel;
import net.hectus.neobb.turn.default_game.mob.*;
import net.hectus.neobb.turn.default_game.other.TBoat;
import net.hectus.neobb.turn.default_game.other.TNoteBlock;
import net.hectus.neobb.turn.default_game.structure.*;
import net.hectus.neobb.turn.default_game.structure.glass_wall.*;
import net.hectus.neobb.turn.default_game.throwable.*;
import net.hectus.neobb.turn.default_game.warp.*;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Modifiers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * Source: <a href="https://docs.google.com/document/d/1y05rFNz7QcvB7yoyqvYnPgb925Leq2P-HV-NKBuJUNg">Official Google Document</a>
 */
public class DefaultGame extends HectusGame {
    public static final GameInfo INFO = new GameInfo(false, true, 1.0, 25, 9, new Time(3, Time.Unit.MINUTES), 5, DefaultShop.class, DefaultItemLoreBuilder.class, List.of(
            TIronTrapdoor.class, TCampfire.class, TCauldron.class, TPackedIce.class, TLever.class, TOakStairs.class, TBlueBed.class,
            TPiston.class, TSponge.class, THayBlock.class, TDriedKelpBlock.class, TRepeater.class, TRedCarpet.class, TGreenWool.class,
            TFire.class, TStonecutter.class, TLava.class, TPurpleWool.class, TGreenBed.class, TRedBed.class, TCyanCarpet.class,
            TSeaLantern.class, TFireCoral.class, TFenceGate.class, TBeeNest.class, TVerdantFroglight.class, TWhiteWool.class,
            TMagmaBlock.class, TSpruceLeaves.class, TBlueIce.class, TLightningRod.class, THoneyBlock.class, TSpruceTrapdoor.class,
            TGoldBlock.class, TSculk.class, TOrangeWool.class, TPinkBed.class, THornCoral.class, TMangroveRoots.class, TComposter.class,
            TSoulSand.class, TBlackWool.class, TNetherrack.class, TPowderSnow.class, TMagentaGlazedTerracotta.class, TRespawnAnchor.class,
            TWater.class, TLightBlueWool.class, TBrainCoralBlock.class, TFireCoralFan.class, TGreenCarpet.class, TDragonHead.class,
            TWhiteTulip.class, TCornflower.class, TWitherRose.class, TBlueOrchid.class, TSunflower.class, TAzureBluet.class, TAllium.class,
            TOrangeTulip.class, TPoppy.class, TRedTulip.class, TPinkTulip.class, TOxeyeDaisy.class, TSunWarp.class, TFrozenWarp.class,
            TCliffWarp.class, TDesertWarp.class, TOceanWarp.class, TWoodWarp.class, TMeadowWarp.class, TRedstoneWarp.class, TVoidWarp.class,
            TMushroomWarp.class, TNetherWarp.class, TNerdWarp.class, TAmethystWarp.class, TEndWarp.class, TBoat.class, TChorusFruit.class,
            TIronShovel.class, TPiglin.class, TPhantom.class, TSheep.class, TBee.class, TAxolotl.class, TEvoker.class, TPufferfish.class,
            TPolarBear.class, TBlaze.class, TSnowball.class, TSplashLevitationPotion.class, TEnderPearl.class, TSplashWaterBottle.class,
            TSplashJumpBoostPotion.class, TBlueGlassWall.class, TOrangeGlassWall.class, TGreenGlassWall.class, TWhiteGlassWall.class,
            TPinkGlassWall.class, TRedGlassWall.class, TGlassWall.class, TDaylightSensorLine.class, TIronBarJail.class,
            TOakDoorTurtling.class, TPumpkinWall.class, TRedstoneWall.class, TDirt.class, TFlowerPot.class, TNoteBlock.class
    ));

    public DefaultGame(Difficulty difficulty, World world, @NotNull List<Player> players) {
        super(difficulty, world, players, new TDefaultWarp(world));
    }

    @Override
    public GameInfo info() {
        return INFO;
    }

    @Override
    public void outOfBoundsAction(@NotNull NeoPlayer player) {
        player.player.damage(2.0);
    }

    @Override
    public @Nullable List<Component> scoreboard(@NotNull NeoPlayer player) {
        Locale l = player.locale();
        try {
            return List.of(
                    MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"),
                    Translation.component(l, "scoreboard.turning").color(Colors.ACCENT)
                            .append(Component.text(currentPlayer() == player ? Translation.string(l, "scoreboard.turning.you") : currentPlayer().player.getName(), Colors.RESET)),
                    Translation.component(l, "scoreboard.time").color(Colors.ACCENT)
                            .append(Component.text(player.game.timeLeft().getPreciselyFormatted(), Colors.RESET)),
                    Translation.component(l, "scoreboard.luck").color(Colors.ACCENT)
                            .append(Component.text(player.luck(), Colors.RESET)),
                    Component.empty(),
                    Component.text("NeoBB-" + NeoBB.VERSION + " (d" + Integer.toHexString(LocalDateTime.now().getDayOfYear()) + "h" + LocalDateTime.now().getHour() + ")", Colors.EXTRA),
                    Component.text("mc.hectus.net", Colors.LINK)
            );
        } catch (Exception e) {
            return List.of(MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"));
        }
    }

    @Override
    public @Nullable Component actionbar(@NotNull NeoPlayer player) {
        Locale l = player.locale();
        if (currentPlayer() == player) {
            if (player.hasModifier(Modifiers.P_DEFAULT_ATTACKED)) {
                if (player.hasModifier(Modifiers.P_DEFAULT_DEFENDED)) {
                    return Translation.component(l, "actionbar.defended_attack").color(Colors.NEUTRAL);
                } else {
                    return Translation.component(l, "actionbar.attacked").color(Colors.NEGATIVE);
                }
            } else {
                return Translation.component(l, "actionbar.you_turning").color(Colors.POSITIVE);
            }
        } else {
            return Translation.component(l, "actionbar.other_turning", currentPlayer().player.getName()).color(Colors.NEUTRAL);
        }
    }
}
