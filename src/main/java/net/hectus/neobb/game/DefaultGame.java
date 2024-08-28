package net.hectus.neobb.game;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.util.GameInfo;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.shop.DefaultShop;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.function.*;
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
import net.hectus.neobb.util.Colors;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class DefaultGame extends BossBarGame {
    public static final GameInfo INFO = new GameInfo(25, 9, new Time(3, Time.Unit.MINUTES), 5, List.of(
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
            TOakDoorTurtling.class, TPumpkinWall.class, TRedstoneWall.class
    ));

    public DefaultGame(boolean ranked, World world, @NotNull List<Player> players) {
        super(ranked, world, players);
        this.shop = new DefaultShop(this);
        this.players.forEach(player -> shop.open(player));
    }

    @Override
    public BossBar initialBossBar() {
        return BossBar.bossBar(Component.text("Turn Countdown: -"), 0.0f, BossBar.Color.YELLOW, BossBar.Overlay.NOTCHED_10);
    }

    @Override
    public void bossBar(@NotNull BossBar bossBar) {
        bossBar.name(Component.text("Turn Countdown: " + turnCountdown + "s"));
        bossBar.progress((float) turnCountdown / INFO.turnTimer());
    }

    @Override
    public GameInfo info() {
        return INFO;
    }

    @Override
    public void turn(@NotNull Turn<?> turn, Cancellable event) {
        if (outOfBounds(turn, event)) return;

        if (!turn.canBeUsed()) {
            super.turn(turn, event);
            turn.player().sendMessage(Component.text("That is not quite how to use this turn...", Colors.NEGATIVE));
            turn.player().player.playSound(turn.player().player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            if (turn.player().hasModifier("attacked") && !turn.player().hasModifier("defended"))
                eliminatePlayer(turn.player());
            return;
        }

        if (turn instanceof CounterFunction counter) {
            if (history.isEmpty()) return;
            Turn<?> last = history.getLast();
            if (counter.counters().stream().anyMatch(filter -> filter.doCounter(last))) {
                counter.counter(turn.player());
            } else {
                super.turn(turn, event);
                turn.player().sendMessage(Translation.component(turn.player().locale(), "gameplay.info.wrong_counter").color(Colors.NEGATIVE));
                turn.player().player.playSound(turn.player().player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                if (turn.player().hasModifier("attacked") && !turn.player().hasModifier("defended"))
                    eliminatePlayer(turn.player());
                return;
            }
        }

        if (turn instanceof AttackFunction) {
            if (!(getNextPlayer().hasModifier("defended") || turn instanceof TPhantom))
                getNextPlayer().addModifier("attacked");
        }

        if (turn instanceof BuffFunction buffFunction) buffFunction.buffs().forEach(b -> b.apply(turn.player()));
        if (turn instanceof DefenseFunction defenseFunction) defenseFunction.applyDefense();
        if (turn instanceof EventFunction eventFunction) eventFunction.triggerEvent();

        super.turn(turn, event);
        if (turn.player().hasModifier("attacked") && !turn.player().hasModifier("defended"))
            eliminatePlayer(turn.player());
    }

    @Override
    public @Nullable List<Component> scoreboard(@NotNull NeoPlayer player) {
        Locale l = player.locale();
        try {
            return List.of(
                    MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"),
                    Translation.component(l, "scoreboard.turning").color(Colors.ACCENT)
                            .append(Component.text(currentPlayer() == player ? "You" : currentPlayer().player.getName(), Colors.RESET)),
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
            if (player.hasModifier("attacked")) {
                if (player.hasModifier("defended")) {
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
