package net.hectus.neobb.game.mode;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.game.util.GameInfo;
import net.hectus.neobb.lore.CardItemLoreBuilder;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.shop.RandomizedShop;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.card_game.*;
import net.hectus.neobb.turn.default_game.warp.TDefaultWarp;
import net.hectus.neobb.util.Colors;
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
 * Source: <a href="https://github.com/TiagoFar78/BlockBattles">Source Code on GitHub</a>
 * <p>Originally named HereGame, because it was based on HereStudio, but renamed to CardGame.
 */
public class CardGame extends Game {
    public static final GameInfo INFO = new GameInfo(true, false, 50.0, 0, 5, new Time(10, Time.Unit.MINUTES), 15, RandomizedShop.class, CardItemLoreBuilder.class, List.of(
            CTChest.class, CTDaylightDetector.class, CTFlowerPot.class, CTJackOLantern.class, CTOakDoor.class, CTOakFenceGate.class,
            CTPointedDripstone.class, CTRedstoneLamp.class, CTTorch.class, CTWaxedExposedCutCopperStairs.class
    ));

    public CardGame(boolean ranked, World world, @NotNull List<Player> players) {
        super(ranked, world, players, new TDefaultWarp(world));
    }

    @Override
    public GameInfo info() {
        return INFO;
    }

    @Override
    public void postTurn(@NotNull Turn<?> turn, boolean skipped) {
        turn.player().inventory.fillInRandomly();
    }

    @Override
    public @Nullable List<Component> scoreboard(@NotNull NeoPlayer player) {
        Locale l = player.locale();
        try {
            return List.of(
                    MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"),
                    Translation.component(l, "scoreboard.turning").color(Colors.BLUE)
                            .append(Component.text(currentPlayer() == player ? "You" : currentPlayer().player.getName(), Colors.RESET)),
                    Translation.component(l, "scoreboard.time").color(Colors.BLUE)
                            .append(Component.text(player.game.timeLeft().getPreciselyFormatted(), Colors.RESET)),
                    Translation.component(l, "scoreboard.health").color(Colors.BLUE)
                            .append(Component.text(player.health(), Colors.RESET)),
                    Component.empty(),
                    Component.text("NeoBB-" + NeoBB.VERSION + " (d" + Integer.toHexString(LocalDateTime.now().getDayOfYear()) + "h" + LocalDateTime.now().getHour() + ")", Colors.EXTRA),
                    Component.text("mc.hectus.net", Colors.LINK)
            );
        } catch (Exception e) {
            return List.of(MiniMessage.miniMessage().deserialize("<gradient:#D068FF:#EC1A3D>BlockBattles<reset><#BF646B>-<#9D9D9D>Alpha"));
        }
    }

    @Override
    public boolean allows(Turn<?> turn) {
        return true;
    }
}
