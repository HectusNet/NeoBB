package net.hectus.neobb.game;

import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.util.GameInfo;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.shop.DefaultShop;
import net.hectus.neobb.turn.*;
import net.hectus.neobb.turn.attributes.function.*;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Cord;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

public class DefaultGame extends Game {
    public static final GameInfo INFO = new GameInfo(40, 9, false, List.of(
            TCauldron.class,
            TIronTrapdoor.class,
            TPurpleWool.class,
            TSpruceTrapdoor.class,
            TGoldBlock.class
    ));

    public DefaultGame(World world, @NotNull List<Player> players, Cord corner1, Cord corner2) throws ReflectiveOperationException {
        super(world, players, corner1, corner2);
        this.shop = new DefaultShop(this);
        this.players.forEach(player -> shop.open(player));
    }

    @Override
    public GameInfo info() {
        return INFO;
    }

    @Override
    public void turn(@NotNull Turn<?> turn) {
        if (!turn.canBeUsed()) return;

        if (turn instanceof CounterFunction counter) {
            Turn<?> last = history.getLast();
            if (counter.counters().stream().anyMatch(filter -> filter.isInstance(last))) {
                turn.player().removeModifier("attacked");
            } else {
                turn.player().sendMessage(Translation.component(turn.player().player.locale(), "turn.issue.wrong_counter"));
                return;
            }
        }

        if (turn instanceof AttackFunction)
            turn.player().addModifier("attacked");

        if (turn instanceof BuffFunction buff) buff.applyBuffs();
        if (turn instanceof DefenseFunction defense) defense.applyDefense();
        if (turn instanceof EventFunction event) event.triggerEvent();

        super.turn(turn);
    }

    @Override
    public @Nullable List<Component> scoreboard(@NotNull NeoPlayer player) {
        Locale l = player.player.locale();
        return List.of(
                miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"),
                Translation.component(l, "scoreboard.turning").color(Colors.ACCENT)
                        .append(Component.text(currentPlayer() == player ? "You" : currentPlayer().player.getName(), Colors.RESET)),
                Translation.component(l, "scoreboard.time").color(Colors.ACCENT)
                        .append(Component.text(currentPlayer() == player ? "You" : currentPlayer().player.getName(), Colors.RESET)),
                Translation.component(l, "scoreboard.luck").color(Colors.ACCENT)
                        .append(Component.text(currentPlayer() == player ? "You" : currentPlayer().player.getName(), Colors.RESET)),
                Component.empty(),
                Component.text("NeoBB-" + NeoBB.VERSION + " (d" + Integer.toHexString(LocalDateTime.now().getDayOfYear()) + "h" + LocalDateTime.now().getHour() + ")", Colors.EXTRA),
                Component.text("mc.hectus.net", Colors.LINK)
        );
    }

    @Override
    public @Nullable Component actionbar(@NotNull NeoPlayer player) {
        Locale l = player.player.locale();
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
