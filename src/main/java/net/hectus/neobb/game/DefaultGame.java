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
import net.hectus.neobb.turn.default_game.item.TChorusFruit;
import net.hectus.neobb.turn.default_game.item.TIronShovel;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Cord;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class DefaultGame extends Game {
    public static final GameInfo INFO = new GameInfo(25, 9, false, List.of(
            TBlackWool.class, TCauldron.class, TCyanCarpet.class, TFire.class, TGoldBlock.class, TGreenCarpet.class,
            TIronTrapdoor.class, TMagentaGlazedTerracotta.class, TMagmaBlock.class, TNetherrack.class, TPurpleWool.class,
            TSculk.class, TSponge.class, TSpruceTrapdoor.class, TStonecutter.class, TChorusFruit.class, TIronShovel.class
    ));

    public DefaultGame(boolean ranked, World world, @NotNull List<Player> players, Cord corner1, Cord corner2) throws ReflectiveOperationException {
        super(ranked, world, players, corner1, corner2);
        this.shop = new DefaultShop(this);
        this.players.forEach(player -> shop.open(player));
    }

    @Override
    public GameInfo info() {
        return INFO;
    }

    @Override
    public void turn(@NotNull Turn<?> turn) {
        if (!turn.canBeUsed()) {
            super.turn(turn);
            turn.player().sendMessage(Component.text("That is not quite how to use this turn...", Colors.NEGATIVE));
            turn.player().player.playSound(turn.player().player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            if (turn.player().hasModifier("attacked"))
                eliminatePlayer(turn.player());
            return;
        }

        if (turn instanceof CounterFunction counter) {
            if (history.isEmpty()) return;
            Turn<?> last = history.getLast();
            if (counter.counters().stream().anyMatch(filter -> filter.doCounter(last))) {
                counter.counter(turn.player());
            } else {
                super.turn(turn);
                turn.player().sendMessage(Translation.component(turn.player().locale(), "gameplay.info.wrong_counter").color(Colors.NEGATIVE));
                turn.player().player.playSound(turn.player().player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                if (turn.player().hasModifier("attacked"))
                    eliminatePlayer(turn.player());
                return;
            }
        }

        if (turn instanceof AttackFunction)
            getNextPlayer().addModifier("attacked");

        if (turn instanceof BuffFunction buff) buff.buffs().forEach(b -> b.apply(turn.player()));
        if (turn instanceof DefenseFunction defense) defense.applyDefense();
        if (turn instanceof EventFunction event) event.triggerEvent();

        super.turn(turn);
        if (turn.player().hasModifier("attacked"))
            eliminatePlayer(turn.player());
    }

    @Override
    public Time initialTime() {
        return new Time(5, Time.Unit.MINUTES);
    }

    @Override
    public @Nullable List<Component> scoreboard(@NotNull NeoPlayer player) {
        Locale l = player.locale();
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
