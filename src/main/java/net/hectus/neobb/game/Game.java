package net.hectus.neobb.game;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.Rating;
import net.hectus.neobb.event.GameEvents;
import net.hectus.neobb.game.util.Arena;
import net.hectus.neobb.game.util.GameInfo;
import net.hectus.neobb.game.util.GameManager;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.player.TargetObj;
import net.hectus.neobb.shop.Shop;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Cord;
import net.hectus.neobb.util.EffectUtil;
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

import java.util.*;

public abstract class Game {
    public final UUID uuid = UUID.randomUUID();
    public final Arena arena;
    protected final boolean ranked;

    protected final List<NeoPlayer> initialPlayers = new ArrayList<>();
    protected final List<NeoPlayer> players = new ArrayList<>();
    protected final List<Turn<?>> history = new ArrayList<>();
    protected final Set<String> modifiers = new HashSet<>();

    protected boolean started;
    protected int turningIndex = 0;
    protected Shop shop;

    public long startTick;
    protected Time timeLeft;

    public Game(boolean ranked, World world, @NotNull List<Player> players, Cord corner1, Cord corner2) {
        this.ranked = ranked;
        this.arena = new Arena(world, corner1, corner2);
        players.stream()
                .map(player -> new NeoPlayer(player, this))
                .peek(initialPlayers::add)
                .forEach(this.players::add);
        GameManager.GAMES.add(this);

        players.forEach(p -> {
            cleanPlayer(p);
            p.setGameMode(GameMode.SURVIVAL);
        });
    }

    public abstract GameInfo info();

    @MustBeInvokedByOverriders
    public void turn(@NotNull Turn<?> turn) {
        turn.apply();

        history.add(turn);
        turn.player().addTurn();

        EffectUtil.applyEffects(turn);

        currentPlayer().player.playSound(currentPlayer().player, Sound.BLOCK_NOTE_BLOCK_BELL, 0.5f, 1.0f);
        initialPlayers.forEach(p -> p.sendMessage(Translation.component(p.locale(), "gameplay.info.turn-used",
                turn.player().player.getName(), Utilities.turnKey(turn.getClass().getSimpleName())).color(Colors.EXTRA)));

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
        if (players.size() == 1) {
            win(players.getFirst());
        }
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

    public final Time timeLeft() {
        return timeLeft;
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
        timeLeft = initialTime();
        startTick = GameEvents.currentTick;
        setStarted(true);
    }

    public void win(@NotNull NeoPlayer player) {
        player.showTitle(Title.title(Translation.component(player.locale(), "gameplay.info.ending.win").color(Colors.POSITIVE),
                Translation.component(player.locale(), "gameplay.info.ending.win-sub").color(Colors.NEUTRAL)));

        player.opponents(false).forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.lose").color(Colors.NEGATIVE),
                Translation.component(p.locale(), "gameplay.info.ending.lose-sub").color(Colors.NEUTRAL))));

        end();
        Rating.updateRankings(List.of(player), player.opponents(false));
    }

    public void draw() {
        players.forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.draw").color(Colors.NEUTRAL),
                Translation.component(p.locale(), "gameplay.info.ending.draw-sub").color(Colors.EXTRA))));

        end();
        Rating.updateRankings(initialPlayers);
    }

    public void giveUp(@NotNull NeoPlayer player) {
        players.forEach(p -> p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.giveup", player.player.getName()).color(Colors.NEUTRAL),
                Translation.component(p.locale(), "gameplay.info.ending.giveup-sub", player.player.getName()).color(Colors.EXTRA))));

        end();
        Rating.updateRankings(player.opponents(false), List.of(player));
    }

    public void end() {
        players.forEach(p -> cleanPlayer(p.player));
        GameManager.GAMES.remove(this);

        Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, () -> players.forEach(p -> p.player.kick(Component.text("Game ended!"))), 100); // 5 Seconds
    }

    private static void cleanPlayer(@NotNull Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        player.getInventory().clear();
        player.clearActivePotionEffects();
    }
}
