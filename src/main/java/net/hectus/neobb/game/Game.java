package net.hectus.neobb.game;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.Rating;
import net.hectus.neobb.event.custom.CancellableImpl;
import net.hectus.neobb.game.util.*;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.player.TargetObj;
import net.hectus.neobb.shop.Shop;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.TTimeLimit;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.warp.TDefaultWarp;
import net.hectus.neobb.turn.default_game.warp.WarpTurn;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Cord;
import net.hectus.neobb.util.Modifiers;
import net.hectus.neobb.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Game extends Modifiers.Modifiable {
    public final String id = Randomizer.generateRandomString(10, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    public final Arena arena;
    public final EffectManager effectManager = new EffectManager();
    public final TurnScheduler turnScheduler = new TurnScheduler();
    public final boolean ranked;

    protected final List<NeoPlayer> initialPlayers = new ArrayList<>();
    protected final List<NeoPlayer> players = new ArrayList<>();
    protected final List<Turn<?>> history = new ArrayList<>();
    protected final Set<Class<? extends Clazz>> allowedClazzes = new HashSet<>();

    protected boolean started;
    protected int turningIndex = 0;
    protected Shop shop;

    protected WarpTurn warp;

    protected Time timeLeft;
    protected int turnCountdown = info().turnTimer();

    public Game(boolean ranked, World world, @NotNull List<Player> players) {
        this.ranked = ranked;
        this.arena = new Arena(this, world);

        players.stream()
                .map(player -> new NeoPlayer(player, this))
                .peek(initialPlayers::add)
                .forEach(this.players::add);

        players.forEach(p -> {
            cleanPlayer(p);
            p.setGameMode(GameMode.SURVIVAL);
        });

        warp(new TDefaultWarp(world));

        // TODO: Support multiple players and make it a circle:
        players.getFirst().teleport(new Location(warp.center.getWorld(), warp.center.x() + 2.5, warp.center.y(), warp.center.z(), 90, 0));
        players.getLast().teleport(new Location(warp.center.getWorld(), warp.center.x() - 2.5, warp.center.y(), warp.center.z(), -90, 0));

        GameManager.add(this);
    }

    public abstract GameInfo info();

    @MustBeInvokedByOverriders
    public void turn(@NotNull Turn<?> turn, Cancellable event) {
        if (outOfBounds(turn.location(), event)) return;

        try {
            if (!(turn instanceof TTimeLimit)) {
                turn.player().inventory.setDeckSlot(turn.player().player.getInventory().getHeldItemSlot(), null, null);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {}

        turn.apply();

        history.add(turn);
        turn.player().addTurn();
        turnScheduler.tick();

        effectManager.applyEffects(turn);
        currentPlayer().player.playSound(currentPlayer().player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1.0f);
        initialPlayers.forEach(p -> p.sendMessage(Translation.component(p.locale(), "gameplay.info.turn-used",
                turn.player().player.getName(), Utilities.turnName(turn.getClass().getSimpleName(), p.locale())).color(Colors.EXTRA)));

        arena.resetCurrentBlocks();
        resetTurnCountdown();
        players.forEach(p -> p.inventory.sync());

        if (turn.player().hasModifier(Modifiers.P_EXTRA_TURN)) {
            turn.player().removeModifier(Modifiers.P_EXTRA_TURN);
        } else {
            moveToNextPlayer();
        }
    }

    /**
     * Verifies if the turn can be used and will return true if it is cancelled and the turn method can exit safely, due to being outside of the arena for example.
     * @return {@code false} if the turn method should abort, {@code true} if it can continue.
     */
    public final boolean outOfBounds(@NotNull Location location, Cancellable event) {
        if (!Cord.ofLocation(location.clone().subtract(warp.location())).inBounds(0, 9, 0, arena.currentPlacedBlocks()[0].length - 1, 0, 9)) {
            event.setCancelled(true);
            return true;
        }
        if (!Cord.ofLocation(location.clone()).inBounds(warp.lowCorner(), warp.highCorner())) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    public @Nullable List<Component> scoreboard(NeoPlayer player) { return null; }
    public @Nullable Component actionbar(NeoPlayer player) { return null; }

    public void gameTick(int tick) {
        if (started) {
            players.forEach(NeoPlayer::tick);
            if (tick % 20 == 0) {
                timeLeft.decrement();
                if (timeLeft.get() == 0)
                    draw(false);
                turnCountdownTick();
            }
        }
    }

    public final List<NeoPlayer> initialPlayers() {
        return initialPlayers;
    }

    public final List<NeoPlayer> players() {
        return players;
    }

    public void eliminatePlayer(@NotNull NeoPlayer player) {
        if (player.hasModifier(Modifiers.P_REVIVE)) {
            player.player.playEffect(EntityEffect.TOTEM_RESURRECT);
            player.sendMessage(Translation.component(player.locale(), "gameplay.info.revive.use").color(Colors.POSITIVE));
            player.removeModifier(Modifiers.P_REVIVE);
            return;
        }

        players.remove(player);
        player.player.setHealth(0.0);

        Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, () -> {
            if (players.size() == 1) {
                win(players.getFirst());
            }
            player.player.teleport(warp.center);
            player.player.setGameMode(GameMode.SPECTATOR);
        }, 20);
    }

    public final @NotNull TargetObj gameTarget(boolean onlyAlive) {
        return new TargetObj(onlyAlive ? players : initialPlayers);
    }

    public final List<Turn<?>> history() {
        return history;
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

    public final Shop shop() {
        return shop;
    }

    public final WarpTurn warp() {
        return warp;
    }

    public void warp(@NotNull WarpTurn warp) {
        this.warp = warp;
        allowedClazzes.clear();
        allowedClazzes.addAll(warp.allows());
        modifiers.removeIf(s -> s.startsWith(Modifiers.G_DEFAULT_WARP_PREVENT_PREFIX));

        players.forEach(p -> {
            if (warp.temperature() == WarpTurn.Temperature.COLD) {
                p.player.setFireTicks(0);
                turnScheduler.removeTask("burn");
            }
        });
    }

    public boolean allows(Turn<?> turn) {
        for (Class<? extends Clazz> clazz : allowedClazzes) {
            if (clazz.isInstance(turn) && (!(turn instanceof WarpTurn) || !hasModifier(Modifiers.G_DEFAULT_WARP_PREVENT_PREFIX + Utilities.camelToSnake(Utilities.counterFilterName(clazz.getSimpleName())))))
                return true;
        }
        return false;
    }

    public final Set<Class<? extends Clazz>> allowedClazzes() {
        return allowedClazzes;
    }

    public final Time timeLeft() {
        return timeLeft;
    }

    public final void turnCountdownTick() {
        turnCountdown--;
        if (turnCountdown <= 0)
            turn(new TTimeLimit(new Time(turnCountdown), currentPlayer()), new CancellableImpl());
    }

    public final void resetTurnCountdown() {
        turnCountdown = info().turnTimer();
    }

    public final void moveToNextPlayer() {
        turningIndex = (turningIndex + 1) % players.size();
    }

    public final NeoPlayer getNextPlayer() {
        return players.get((turningIndex + 1) % players.size());
    }

    public final NeoPlayer currentPlayer() {
        if (turningIndex >= players.size())
            turningIndex = 0;
        return players.get(turningIndex);
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
        timeLeft = new Time(info().totalTime());
        timeLeft.setAllowNegatives(true);
        setStarted(true);
    }

    public void end(boolean force) {
        initialPlayers.forEach(p -> cleanPlayer(p.player));
        GameManager.remove(this);
        effectManager.clearHighlight();

        if (force) return;
        Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, () -> {
            initialPlayers.forEach(p -> p.player.kick(Component.text("Game ended!")));
            if (NeoBB.PRODUCTION) {
                Bukkit.getServer().shutdown();
            } else {
                arena.cleanUp();
            }
        }, 100); // 5 Seconds
    }

    public final void win(@NotNull NeoPlayer player) {
        player.showTitle(Title.title(Translation.component(player.locale(), "gameplay.info.ending.win").color(Colors.POSITIVE),
                Translation.component(player.locale(), "gameplay.info.ending.win-sub").color(Colors.NEUTRAL)));

        player.opponents(false).forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.lose").color(Colors.NEGATIVE),
                Translation.component(p.locale(), "gameplay.info.ending.lose-sub").color(Colors.NEUTRAL))));

        end(false);
        if (ranked) Rating.updateRankingsWin(player, player.opponents(false).getFirst());
    }

    public final void draw(boolean force) {
        players.forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.draw").color(Colors.NEUTRAL),
                Translation.component(p.locale(), "gameplay.info.ending.draw-sub").color(Colors.EXTRA))));

        end(force);
        if (ranked) Rating.updateRankingsDraw(initialPlayers.get(0), initialPlayers.get(1));
    }

    public final void giveUp(@NotNull NeoPlayer player) {
        players.forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.giveup", player.player.getName()).color(Colors.NEUTRAL),
                Translation.component(p.locale(), "gameplay.info.ending.giveup-sub", player.player.getName()).color(Colors.EXTRA))));

        end(false);
        if (ranked) Rating.updateRankingsWin(player.opponents(false).getFirst(), player);
    }

    private static void cleanPlayer(@NotNull Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        player.closeInventory();
        player.getInventory().clear();
        player.clearActivePotionEffects();
    }
}
