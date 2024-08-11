package net.hectus.neobb.game;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.text.Formatter;
import net.hectus.neobb.game.util.Arena;
import net.hectus.neobb.game.util.GameInfo;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.player.Target;
import net.hectus.neobb.player.TargetObj;
import net.hectus.neobb.shop.Shop;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Cord;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class Game {
    public final UUID uuid = UUID.randomUUID();
    public final Arena arena;

    protected final List<NeoPlayer> initialPlayers = new ArrayList<>();
    protected final List<NeoPlayer> players = new ArrayList<>();
    protected final List<Turn<?>> history = new ArrayList<>();
    protected final Set<String> modifiers = new HashSet<>();

    protected boolean started;
    protected int turningIndex = 0;
    protected Shop shop;

    public Game(World world, @NotNull List<Player> players, Cord corner1, Cord corner2) {
        this.arena = new Arena(world, corner1, corner2);
        players.stream()
                .map(player -> new NeoPlayer(player, this))
                .peek(initialPlayers::add)
                .forEach(this.players::add);
        GameManager.GAMES.add(this);
    }

    public abstract GameInfo info();

    @MustBeInvokedByOverriders
    public void turn(@NotNull Turn<?> turn) {
        turn.apply();
        history.add(turn);
        initialPlayers.forEach(p -> p.sendMessage(Translation.component(p.player.locale(), "gameplay.info.turn-used",
                turn.player().player.getName(), Formatter.toPascalCase(turn.item().getType().name()))));
        moveToNextPlayer();
    }

    public @Nullable List<Component> scoreboard(NeoPlayer player) { return null; }
    public @Nullable Component actionbar(NeoPlayer player) { return null; }

    public void gameTick() {
        if (started) {
            players.forEach(NeoPlayer::tick);
        }
    }

    public final List<NeoPlayer> initialPlayers() {
        return initialPlayers;
    }

    public final List<NeoPlayer> players() {
        return players;
    }

    public final @NotNull TargetObj gameTarget(boolean onlyAlive) {
        return new TargetObj(onlyAlive ? players : initialPlayers);
    }

    public final List<Turn<?>> history() {
        return history;
    }

    public final void addModifier(String modifier) {
        modifiers.add(modifier);
    }

    public final void removeModifier(String modifier) {
        modifiers.remove(modifier);
    }

    public final boolean hasModifier(String modifier) {
        return modifiers.contains(modifier);
    }

    public final World world() {
        return arena.world;
    }

    public final boolean started() {
        return started;
    }

    public final void setStarted(boolean started) {
        this.started = started;
    }

    public final void moveToNextPlayer() {
        turningIndex = (turningIndex + 1) % players.size();
    }

    public final NeoPlayer currentPlayer() {
        return players.get(turningIndex);
    }

    public final Target opponentPlayers() {
        return currentPlayer().opponentTarget();
    }

    public final Target opponentPlayers(@NotNull NeoPlayer player) {
        return player.opponentTarget();
    }

    public final @Nullable NeoPlayer player(Player player, boolean onlyAlive) {
        for (NeoPlayer p : onlyAlive ? players : initialPlayers) {
            if (p.player == player) return p;
        }
        return null;
    }

    public final boolean allShopDone() {
        for (NeoPlayer player : players) {
            if (!player.inventory.shopDone()) return false;
        }
        return true;
    }

    public void start() {
        setStarted(true);
    }

    public void win(@NotNull NeoPlayer player) {
        player.showTitle(Title.title(Translation.component(player.player.locale(), "gameplay.info.ending.win").color(Colors.POSITIVE),
                Translation.component(player.player.locale(), "gameplay.info.ending.win-sub").color(Colors.NEUTRAL)));

        player.opponents().forEach(p -> p.showTitle(Title.title(Translation.component(p.player.locale(), "gameplay.info.ending.lose").color(Colors.NEGATIVE),
                Translation.component(p.player.locale(), "gameplay.info.ending.lose-sub").color(Colors.NEUTRAL))));
        end();
    }

    public void lose(@NotNull NeoPlayer player) {
        player.showTitle(Title.title(Translation.component(player.player.locale(), "gameplay.info.ending.lose").color(Colors.NEGATIVE),
                Translation.component(player.player.locale(), "gameplay.info.ending.lose-sub").color(Colors.NEUTRAL)));

        player.opponents().forEach(p -> p.showTitle(Title.title(Translation.component(p.player.locale(), "gameplay.info.ending.win").color(Colors.POSITIVE),
                Translation.component(p.player.locale(), "gameplay.info.ending.win-sub").color(Colors.NEUTRAL))));
        end();
    }

    public void draw() {
        players.forEach(p -> p.showTitle(Title.title(Translation.component(p.player.locale(), "gameplay.info.ending.draw").color(Colors.NEUTRAL),
                Translation.component(p.player.locale(), "gameplay.info.ending.draw-sub").color(Colors.EXTRA))));
        end();
    }

    public void giveUp(NeoPlayer player) {
        players.forEach(p -> p.showTitle(Title.title(Translation.component(p.player.locale(), "gameplay.info.ending.giveup", player.player.getName()).color(Colors.NEUTRAL),
                Translation.component(p.player.locale(), "gameplay.info.ending.giveup-sub", player.player.getName()).color(Colors.EXTRA))));
        end();
    }

    public void end() {
        players.forEach(p -> p.player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard()));

        // TODO: Add ELO and stuff to the players.

        GameManager.GAMES.remove(this);
    }
}
