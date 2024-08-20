package net.hectus.neobb.game;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.Rating;
import net.hectus.neobb.event.GameEvents;
import net.hectus.neobb.game.util.Arena;
import net.hectus.neobb.game.util.EffectManager;
import net.hectus.neobb.game.util.GameInfo;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.player.TargetObj;
import net.hectus.neobb.shop.Shop;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.TTimeLimit;
import net.hectus.neobb.turn.default_game.warp.TDefaultWarp;
import net.hectus.neobb.turn.default_game.warp.Warp;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Game {
    public final String id = Randomizer.generateRandomString(10, "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
    public final Arena arena;
    public final EffectManager effectManager;
    public final boolean ranked;

    protected final List<NeoPlayer> initialPlayers = new ArrayList<>();
    protected final List<NeoPlayer> players = new ArrayList<>();
    protected final List<Turn<?>> history = new ArrayList<>();
    protected final Set<String> modifiers = new HashSet<>();

    protected boolean started;
    protected int turningIndex = 0;
    protected Shop shop;

    protected Warp warp;
    protected Set<Class<? extends Clazz>> allowedClazzes;

    protected long startTick;
    protected Time timeLeft;
    protected int turnCountdown;

    public Game(boolean ranked, World world, @NotNull List<Player> players) {
        this.ranked = ranked;
        this.arena = new Arena(this, world);
        this.effectManager = new EffectManager(this);

        players.stream()
                .map(player -> new NeoPlayer(player, this))
                .peek(initialPlayers::add)
                .forEach(this.players::add);

        GameManager.GAMES.add(this);

        players.forEach(p -> {
            cleanPlayer(p);
            p.setGameMode(GameMode.SURVIVAL);
        });

        warp = new TDefaultWarp(world);
    }

    public abstract GameInfo info();

    @MustBeInvokedByOverriders
    public void turn(@NotNull Turn<?> turn) {
        try {
            turn.player().inventory.setDeckSlot(turn.player().player.getInventory().getHeldItemSlot(), null, null);
        } catch (ArrayIndexOutOfBoundsException ignored) {}

        turn.apply();

        history.add(turn);
        turn.player().addTurn();

        effectManager.applyEffects(turn);

        currentPlayer().player.playSound(currentPlayer().player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1.0f);
        initialPlayers.forEach(p -> p.sendMessage(Translation.component(p.locale(), "gameplay.info.turn-used",
                turn.player().player.getName(), Utilities.turnKey(turn.getClass().getSimpleName())).color(Colors.EXTRA)));

        arena.resetCurrentBlocks();
        resetTurnCountdown();
        moveToNextPlayer();
    }

    public Time initialTime() {
        Time time = new Time(-1);
        time.setAllowNegatives(true);
        return time;
    }

    public @Nullable List<Component> scoreboard(NeoPlayer player) { return null; }
    public @Nullable Component actionbar(NeoPlayer player) { return null; }

    public void gameTick() {
        if (started) {
            players.forEach(NeoPlayer::tick);

            if (GameEvents.currentTick - startTick % 20 == 0) {
                timeLeft.decrement();
                if (timeLeft.get() == 0)
                    draw();

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

    public final void eliminatePlayer(NeoPlayer player) {
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

    public final Shop shop() {
        return shop;
    }

    public Warp warp() {
        return warp;
    }

    public void warp(@NotNull Warp warp) {
        this.warp = warp;
        this.allowedClazzes.clear();
        this.allowedClazzes.addAll(warp.allows());
    }

    public final Time timeLeft() {
        return timeLeft;
    }

    public final int turnCountdown() {
        return turnCountdown;
    }

    public final void turnCountdownTick() {
        turnCountdown -= 1;
        if (turnCountdown == 0)
            turn(new TTimeLimit(new Time(turnCountdown), currentPlayer()));
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
        startTick = GameEvents.currentTick;
        setStarted(true);
    }

    public void end() {
        players.forEach(p -> cleanPlayer(p.player));
        GameManager.GAMES.remove(this);
        effectManager.clearHighlight();

        Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, () -> players.forEach(p -> p.player.kick(Component.text("Game ended!"))), 100); // 5 Seconds
    }

    private static void cleanPlayer(@NotNull Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        player.getInventory().clear();
        player.clearActivePotionEffects();
    }

    public final void win(@NotNull NeoPlayer player) {
        player.showTitle(Title.title(Translation.component(player.locale(), "gameplay.info.ending.win").color(Colors.POSITIVE),
                Translation.component(player.locale(), "gameplay.info.ending.win-sub").color(Colors.NEUTRAL)));

        player.opponents(false).forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.lose").color(Colors.NEGATIVE),
                Translation.component(p.locale(), "gameplay.info.ending.lose-sub").color(Colors.NEUTRAL))));

        end();
        if (ranked) Rating.updateRankings(List.of(player), player.opponents(false));
    }

    public final void draw() {
        players.forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.draw").color(Colors.NEUTRAL),
                Translation.component(p.locale(), "gameplay.info.ending.draw-sub").color(Colors.EXTRA))));

        end();
        if (ranked) Rating.updateRankings(initialPlayers);
    }

    public final void giveUp(@NotNull NeoPlayer player) {
        players.forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.giveup", player.player.getName()).color(Colors.NEUTRAL),
                Translation.component(p.locale(), "gameplay.info.ending.giveup-sub", player.player.getName()).color(Colors.EXTRA))));

        end();
        if (ranked) Rating.updateRankings(player.opponents(false), List.of(player));
    }
}
