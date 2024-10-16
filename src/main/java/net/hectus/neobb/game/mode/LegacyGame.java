package net.hectus.neobb.game.mode;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.game.HectusGame;
import net.hectus.neobb.game.util.GameInfo;
import net.hectus.neobb.lore.LegacyItemLoreBuilder;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.shop.DefaultShop;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.TTimeLimit;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.block.*;
import net.hectus.neobb.turn.default_game.flower.*;
import net.hectus.neobb.turn.default_game.item.TChorusFruit;
import net.hectus.neobb.turn.default_game.item.TIronShovel;
import net.hectus.neobb.turn.default_game.mob.*;
import net.hectus.neobb.turn.default_game.other.TBoat;
import net.hectus.neobb.turn.default_game.structure.*;
import net.hectus.neobb.turn.default_game.structure.glass_wall.*;
import net.hectus.neobb.turn.default_game.throwable.*;
import net.hectus.neobb.turn.default_game.warp.*;
import net.hectus.neobb.turn.legacy_game.block.LTPurpleWool;
import net.hectus.neobb.turn.legacy_game.block.LTTnt;
import net.hectus.neobb.turn.legacy_game.item.LTDinnerboneTag;
import net.hectus.neobb.turn.legacy_game.item.LTEnchantedGoldenApple;
import net.hectus.neobb.turn.legacy_game.item.LTLightningTrident;
import net.hectus.neobb.turn.legacy_game.other.LTNoteBlock;
import net.hectus.neobb.turn.legacy_game.structure.LTNetherPortal;
import net.hectus.neobb.turn.legacy_game.warp.LTDefaultWarp;
import net.hectus.neobb.turn.legacy_game.warp.LTRedstoneWarp;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Modifiers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class LegacyGame extends HectusGame {
    public static final GameInfo INFO = new GameInfo(true, 64, 9, new Time(15, Time.Unit.MINUTES), 15, DefaultShop.class, LegacyItemLoreBuilder.class, List.of(
            TIronTrapdoor.class, TCampfire.class, TCauldron.class, TPackedIce.class, TLever.class, TOakStairs.class, TBlueBed.class,
            TPiston.class, TSponge.class, THayBlock.class, TDriedKelpBlock.class, TRepeater.class, TRedCarpet.class, TGreenWool.class,
            TFire.class, TStonecutter.class, TLava.class, TGreenBed.class, TRedBed.class, TCyanCarpet.class, TSeaLantern.class,
            TFireCoral.class, TFenceGate.class, TBeeNest.class, TVerdantFroglight.class, TWhiteWool.class, TMagmaBlock.class,
            TSpruceLeaves.class, TBlueIce.class, TLightningRod.class, THoneyBlock.class, TSpruceTrapdoor.class, TGoldBlock.class,
            TSculk.class, TOrangeWool.class, TPinkBed.class, THornCoral.class, TMangroveRoots.class, TComposter.class, TSoulSand.class,
            TBlackWool.class, TNetherrack.class, TPowderSnow.class, TMagentaGlazedTerracotta.class, TRespawnAnchor.class, TWater.class,
            TLightBlueWool.class, TBrainCoralBlock.class, TFireCoralFan.class, TGreenCarpet.class, TDragonHead.class, TWhiteTulip.class,
            TCornflower.class, TWitherRose.class, TBlueOrchid.class, TSunflower.class, TAzureBluet.class, TAllium.class, TOrangeTulip.class,
            TPoppy.class, TRedTulip.class, TPinkTulip.class, TOxeyeDaisy.class, TSunWarp.class, TFrozenWarp.class, TCliffWarp.class,
            TDesertWarp.class, TOceanWarp.class, TWoodWarp.class, TMeadowWarp.class, TRedstoneWarp.class, TVoidWarp.class,
            TMushroomWarp.class, TNetherWarp.class, TNerdWarp.class, TAmethystWarp.class, TEndWarp.class, TBoat.class, TChorusFruit.class,
            TIronShovel.class, TPiglin.class, TPhantom.class, TSheep.class, TBee.class, TAxolotl.class, TEvoker.class, TPufferfish.class,
            TPolarBear.class, TBlaze.class, TSnowball.class, TSplashLevitationPotion.class, TEnderPearl.class, TSplashWaterBottle.class,
            TSplashJumpBoostPotion.class, TBlueGlassWall.class, TOrangeGlassWall.class, TGreenGlassWall.class, TWhiteGlassWall.class,
            TPinkGlassWall.class, TRedGlassWall.class, TGlassWall.class, TDaylightSensorLine.class, TIronBarJail.class,
            TOakDoorTurtling.class, TPumpkinWall.class, TRedstoneWall.class, TDirt.class, TFlowerPot.class, LTDinnerboneTag.class,
            LTLightningTrident.class, LTPurpleWool.class, LTTnt.class, LTNetherPortal.class, LTEnchantedGoldenApple.class, LTNoteBlock.class
    ));

    public LegacyGame(boolean ranked, World world, @NotNull List<Player> players) {
        super(ranked, world, players, new LTDefaultWarp(world));
    }

    @Override
    public GameInfo info() {
        return INFO;
    }

    @Override
    public boolean preTurn(Turn<?> turn) {
        if (turn instanceof TTimeLimit) {
            eliminatePlayer(turn.player());
            return true;
        }
        if (history.isEmpty() && (turn instanceof AttackFunction || turn instanceof WarpTurn)) {
            turn.player().sendMessage(Component.text("You cannot use attacks or warps as the first turn.", Colors.NEGATIVE));
            turn.player().player.playSound(turn.player().player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }
        return false;
    }

    @Override
    public boolean executeTurn(Turn<?> turn) {
        if (warp instanceof LTRedstoneWarp && turn instanceof RedstoneClazz)
            turn.player().addLuck(5);
        return super.executeTurn(turn);
    }

    @Override
    public @Nullable List<Component> scoreboard(@NotNull NeoPlayer player) {
        Locale l = player.locale();
        try {
            return List.of(
                    MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"),
                    Component.empty(),
                    Component.text(player.player.getName(), Colors.EXTRA),
                    Component.empty(),
                    Translation.component(l, "scoreboard.turning").color(Colors.EXTRA)
                            .append(Component.text(currentPlayer().player.getName())),
                    Component.empty(),
                    Translation.component(l, "scoreboard.luck").color(Colors.EXTRA)
                            .append(Component.text(player.luck(), Colors.RESET)),
                    Component.empty(),
                    Translation.component(l, "scoreboard.attacked").color(Colors.EXTRA)
                            .append(Translation.component(l, "shop.filter." + (player.hasModifier(Modifiers.P_DEFAULT_ATTACKED) ? "yes" : "no")).color(Colors.RESET)),
                    Translation.component(l, "scoreboard.defended").color(Colors.EXTRA)
                            .append(Translation.component(l, "shop.filter." + (player.hasModifier(Modifiers.P_DEFAULT_DEFENDED) ? "yes" : "no")).color(Colors.RESET)),
                    Translation.component(l, "scoreboard.frozen").color(Colors.EXTRA)
                            .append(Translation.component(l, "shop.filter." + (player.hasModifier(Modifiers.P_NO_MOVE) ? "yes" : "no")).color(Colors.RESET))
            );
        } catch (Exception e) {
            return List.of(MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"));
        }
    }
}
