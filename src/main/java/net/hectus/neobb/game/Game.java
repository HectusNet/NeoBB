package net.hectus.neobb.game;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.Rating;
import net.hectus.neobb.cosmetic.EffectManager;
import net.hectus.neobb.event.custom.CancellableImpl;
import net.hectus.neobb.game.util.Difficulty;
import net.hectus.neobb.game.util.*;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.player.TargetObj;
import net.hectus.neobb.turn.DummyTurn;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.TTimeLimit;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.warp.TDefaultWarp;
import net.hectus.neobb.turn.default_game.warp.WarpTurn;
import net.hectus.neobb.turn.person_game.categorization.WinConCategory;
import net.hectus.neobb.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
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
    public final Difficulty difficulty;

    protected final List<NeoPlayer> initialPlayers = new ArrayList<>();
    protected final List<NeoPlayer> players = new ArrayList<>();
    protected final List<Turn<?>> history = new ArrayList<>();
    protected final Set<Class<? extends Clazz>> allowedClazzes = new HashSet<>();

    protected boolean started;
    protected int turningIndex = 0;

    protected WarpTurn warp;
    protected MinecraftTime time;

    protected Time timeLeft;
    protected int turnCountdown;

    protected Game(World world, Player player) {
        this.difficulty = Difficulty.NORMAL;
        this.arena = new Arena(this, world);
        this.warp = new TDefaultWarp(world);
        resetTurnCountdown();

        NeoPlayer p = new NeoPlayer(player, this);
        initialPlayers.add(p);
        players.add(p);
    }

    protected Game(Difficulty difficulty, World world, @NotNull List<Player> players, WarpTurn defaultWarp) {
        this.difficulty = difficulty;
        this.arena = new Arena(this, world);
        this.warp = defaultWarp;
        this.allowedClazzes.addAll(warp.allows());
        resetTurnCountdown();

        players.forEach(p -> {
            cleanPlayer(p);
            p.setGameMode(GameMode.SURVIVAL);
        });

        double radius = 2.5;
        int playerCount = players.size();

        for (int i = 0; i < playerCount; i++) {
            double angle = 2 * Math.PI * i / playerCount;
            players.get(i).teleport(new Location(warp.center.getWorld(),
                    warp.center.x() + radius * Math.cos(angle),
                    warp.center.y(),
                    warp.center.z() + radius * Math.sin(angle),
                    (float) Math.toDegrees(angle + Math.PI / 2), 0 // No, I am not a math guy...
            ));
        }

        players.stream()
                .map(player -> new NeoPlayer(player, this))
                .peek(initialPlayers::add)
                .forEach(this.players::add);

        NeoBB.LOG.info("{}: Initialized the game.", id);
        GameManager.add(this);
    }

    public abstract GameInfo info();

    public final void turn(@NotNull Turn<?> turn, Cancellable event) {
        NeoPlayer player = turn.player();

        if (outOfBounds(turn.location(), event)) {
            NeoBB.LOG.info("{}: {} used {} out of bounds.", id, player.player.getName(), turn.getClass().getName().replace("net.hectus.neobb.turn.", ""));
            event.setCancelled(true);
            return;
        }

        if (turn instanceof DummyTurn) {
            history.add(turn);
            return;
        }

        if (difficulty.usageRules && turn.unusable()) {
            NeoBB.LOG.info("{}: {} used {} incorrectly.", id, player.player.getName(), turn.getClass().getName().replace("net.hectus.neobb.turn.", ""));
            player.sendMessage(Component.text("That is not quite how to use this turn...", Colors.NEGATIVE));
            player.player.playSound(player.player, Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            nextTurn(turn);
        }

        boolean skipped = preTurn(turn);

        if (!(turn instanceof TTimeLimit)) {
            try {
                player.inventory.setDeckSlot(player.player.getInventory().getHeldItemSlot(), null, null);
            } catch (ArrayIndexOutOfBoundsException ignored) {}
        }

        if (!skipped && executeTurn(turn)) {
            turn.apply();
            if (turn.damage() != 0.0) {
                if (turn instanceof WinConCategory) {
                    player.piercingDamage(turn.damage());
                } else {
                    player.damage(turn.damage());
                }
            }

            effectManager.applyEffects(turn);
            player.player.playSound(player.player, player.cosmeticPlaceSound(), 0.5f, 1.0f);
            initialPlayers.forEach(p -> p.sendMessage(Translation.component(p.locale(), "gameplay.info.turn-used",
                    player.player.getName(), Utilities.turnName(turn.getClass().getName().replace("net.hectus.neobb.turn.", ""), p.locale())).color(Colors.EXTRA)));
        }

        nextTurn(turn);
        postTurn(turn, skipped);
    }

    public final void nextTurn(@NotNull Turn<?> turn) {
        history.add(turn);
        turn.player().addTurn();
        turnScheduler.tick();

        arena.resetCurrentBlocks();
        resetTurnCountdown();
        players.forEach(p -> p.inventory.sync());

        if (turn.player().hasModifier(Modifiers.P_EXTRA_TURN)) {
            turn.player().removeModifier(Modifiers.P_EXTRA_TURN);
        } else {
            moveToNextPlayer();
        }
        turn.player().validate();

        NeoBB.LOG.info("{}: {} used {}.", id, turn.player().player.getName(), turn.getClass().getName().replace("net.hectus.neobb.turn.", ""));
    }

    /**
     * This is called after the placement and usability were validated.
     * @return {@code true} if the turn should be skipped, {@code false} otherwise.
     */
    public boolean preTurn(Turn<?> turn) { return false; }

    /**
     * This is executed right before {@link Turn#apply()} is called.
     * If the turn is skipped, this will never be executed.
     * @return {@code true} if {@link Turn#apply()} should be called, {@code false} otherwise.
     */
    public boolean executeTurn(Turn<?> turn) { return true; }

    /**
     * This is called after the turn finished.
     * It will be called no matter if the turn got skipped or not.
     * @param skipped If this turn was skipped.
     */
    public void postTurn(Turn<?> turn, boolean skipped) {}

    /**
     * Verifies if the turn can be used and will return true if it is cancelled and the turn method can exit safely, due to being outside of the arena for example.
     * @return {@code false} if the turn method should abort, {@code true} if it can continue.
     */
    public final boolean outOfBounds(@NotNull Location location, Cancellable event) {
        if (Cord.ofLocation(location.clone()).outOfBounds(warp.lowCorner(), warp.highCorner())) {
            event.setCancelled(true);
            return true;
        }
        return false;
    }

    public void outOfBoundsAction(NeoPlayer player) {
        eliminatePlayer(player);
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
            NeoBB.LOG.info("{}: {} used a revive.", id, player.player.getName());
            player.removeModifier(Modifiers.P_REVIVE);
            return;
        }

        players.remove(player);

        player.cosmeticDeathAnimation().play(player);
        player.player.setHealth(0.0);
        NeoBB.LOG.info("{}: {} got eliminated.", id, player.player.getName());

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

    public final WarpTurn warp() {
        return warp;
    }

    public void warp(@NotNull WarpTurn warp) {
        NeoBB.LOG.info("{}: {} warped from {} to {}.", id, warp.player().player.getName(), this.warp.name, warp.name);

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

    public MinecraftTime time() {
        return time;
    }

    public void time(@NotNull MinecraftTime time) {
        this.time = time;
        world().setTime(time.time);
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
        if (turnCountdown <= 0) {
            if (history.size() >= players.size() && history.subList(history.size() - players.size(), history.size()).stream().allMatch(t -> t instanceof TTimeLimit)) {
                players.forEach(p -> p.sendMessage(Translation.component(p.locale(), "gameplay.info.ending.too-slow").color(Colors.NEUTRAL)));
                draw(false);
            } else {
                turn(new TTimeLimit(new Time(turnCountdown), currentPlayer()), new CancellableImpl());
            }
        }
    }

    public final void resetTurnCountdown() {
        turnCountdown = (int) (info().turnTimer() * difficulty.timeMultiplier);
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
        timeLeft = new Time((long) (info().totalTime().get() * difficulty.timeMultiplier));
        timeLeft.setAllowNegatives(true);
        resetTurnCountdown();

        NeoBB.LOG.info("{}: Started the game.", id);
        setStarted(true);

        if (info().showIntro()) {
            NeoPlayer current = players.getFirst();
            current.showTitle(Title.title(Component.text("You go first!", Colors.POSITIVE), Component.text("Good Luck!", Colors.NEUTRAL)));
            current.opponents(false).forEach(p -> p.showTitle(Title.title(Component.text(current.player.getName() + " goes first!", Colors.RED), Component.text("Good Luck!", Colors.NEUTRAL))));
        }
    }

    public void end(boolean force) {
        initialPlayers.forEach(p -> cleanPlayer(p.player));
        GameManager.remove(this);
        effectManager.clearHighlight();

        if (force) {
            NeoBB.LOG.info("{}: Stopped the game forcefully.", id);
            return;
        } else {
            NeoBB.LOG.info("{}: Stopped the game gracefully.", id);
        }
        Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, () -> {
            initialPlayers.forEach(p -> p.player.kick(Component.text("Game ended!")));
            if (NeoBB.PRODUCTION) {
                Bukkit.getServer().shutdown();
            } else {
                time(MinecraftTime.DAY);
                arena.clear();
            }
        }, 100); // 5 Seconds
    }

    public final void win(@NotNull NeoPlayer player) {
        player.showTitle(Title.title(Translation.component(player.locale(), "gameplay.info.ending.win").color(Colors.POSITIVE),
                Translation.component(player.locale(), "gameplay.info.ending.win-sub").color(Colors.NEUTRAL)));
        player.opponents(false).forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.lose").color(Colors.NEGATIVE),
                Translation.component(p.locale(), "gameplay.info.ending.lose-sub").color(Colors.NEUTRAL))));
        NeoBB.LOG.info("{}: {} won the game.", id, player.player.getName());

        player.cosmeticWinAnimation().play(player);

        end(false);
        if (difficulty.allowRanking)
            Rating.updateRankingsWin(player, player.opponents(false).getFirst(), difficulty.rankingMultiplier); // TODO: Support multiple losers!
    }

    public final void draw(boolean force) {
        players.forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.draw").color(Colors.NEUTRAL),
                Translation.component(p.locale(), "gameplay.info.ending.draw-sub").color(Colors.EXTRA))));
        NeoBB.LOG.info("{}: The game resulted in a draw.", id);

        end(force);
        if (difficulty.allowRanking)
            Rating.updateRankingsDraw(initialPlayers.get(0), initialPlayers.get(1), difficulty.rankingMultiplier); // TODO: Support multiple winners/losers!
    }

    public final void giveUp(@NotNull NeoPlayer player) {
        players.forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.giveup", player.player.getName()).color(Colors.NEUTRAL),
                Translation.component(p.locale(), "gameplay.info.ending.giveup-sub", player.player.getName()).color(Colors.EXTRA))));
        NeoBB.LOG.info("{}: {} gave up.", id, player.player.getName());

        player.opponents(true).forEach(p -> p.cosmeticWinAnimation().play(p));

        end(false);
        if (difficulty.allowRanking)
            Rating.updateRankingsWin(player.opponents(false).getFirst(), player, difficulty.rankingMultiplier); // TODO: Support multiple winners!
    }

    private static void cleanPlayer(@NotNull Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        player.closeInventory();
        player.getInventory().clear();
        player.clearActivePotionEffects();
    }
}
