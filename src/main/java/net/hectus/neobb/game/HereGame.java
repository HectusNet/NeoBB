package net.hectus.neobb.game;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.util.GameInfo;
import net.hectus.neobb.lore.HereItemLoreBuilder;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.shop.RandomizedShop;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.here_game.*;
import net.hectus.neobb.util.Colors;
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

public class HereGame extends Game {
    public static final GameInfo INFO = new GameInfo(0, 5, new Time(10, Time.Unit.MINUTES), 15, List.of(
            HTChest.class, HTDaylightDetector.class, HTFlowerPot.class, HTJackOLantern.class, HTOakDoor.class, HTOakFenceGate.class,
            HTPointedDripstone.class, HTRedstoneLamp.class, HTTorch.class, HTWaxedExposedCutCopperStairs.class
    ));

    public HereGame(boolean ranked, World world, @NotNull List<Player> players) {
        super(ranked, world, players);
        this.shop = new RandomizedShop(this, new HereItemLoreBuilder());
        this.players.forEach(player -> shop.open(player));
    }

    @Override
    public GameInfo info() {
        return INFO;
    }

    @Override
    public void turn(@NotNull Turn<?> turn, Cancellable event) {
        if (outOfBounds(turn.location(), event)) return;

        if (turn.unusable()) {
            super.turn(turn, event);
            turn.player().inventory.fillInRandomly();
            turn.player().sendMessage(Component.text("That is not quite how to use this turn...", Colors.NEGATIVE));
            turn.player().player.playSound(turn.player().player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }

        super.turn(turn, event);
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
