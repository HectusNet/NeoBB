package net.hectus.bb.game;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.storing.Pair;
import net.hectus.bb.player.PlayerData;
import net.hectus.bb.shop.PreGameShop;
import net.hectus.bb.turn.Turn;
import net.hectus.bb.turn.TurnData;
import net.hectus.bb.turn.buff.Buff;
import net.hectus.bb.turn.effective.Attack;
import net.hectus.bb.turn.effective.Burning;
import net.hectus.bb.turn.effective.Defense;
import net.hectus.bb.warp.Warp;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.World;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {
    private final World world;
    private final List<TurnData> history = new ArrayList<>(); // LinkedList might actually be more performant here, not sure.
    private final List<PlayerData> players = new ArrayList<>();
    private PlayerData turning;
    private Warp warp = Warp.DEFAULT;

    public Game(World world, Player challenger, Player challenged) {
        this.world = world;
        this.players.add(new PlayerData(challenger, this));
        this.players.add(new PlayerData(challenged, this));
        this.turning = this.players.get(1);
        this.players.forEach(PreGameShop::menu);
    }

    public List<PlayerData> players() {
        return players;
    }

    public PlayerData turning() {
        return turning;
    }

    public void swapTurning() {
        turning = getOpponent(turning);
    }

    public PlayerData getOpponent(PlayerData playerData) {
        return players.get(players.indexOf(playerData) == 0 ? 1 : 0);
    }

    public Warp warp() {
        return warp;
    }

    public void warp(Warp warp) {
        this.warp = warp;
    }

    // ========== ENDING LOGIC ==========

    public void win(@NotNull PlayerData player) {
        Player p = player.player();
        p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.win").color(NamedTextColor.GREEN),
                Translation.component(p.locale(), "gameplay.info.ending.win-sub").color(NamedTextColor.GOLD)));

        Player o = getOpponent(player).player();
        o.showTitle(Title.title(Translation.component(o.locale(), "gameplay.info.ending.lose").color(NamedTextColor.RED),
                Translation.component(o.locale(), "gameplay.info.ending.lose-sub").color(NamedTextColor.GOLD)));
    }

    public void lose(PlayerData player) {
        win(getOpponent(player));
    }

    public void giveUp(PlayerData player) {
        for (PlayerData p : players) {
            p.player().showTitle(Title.title(Translation.component(p.player().locale(), "gameplay.info.ending.giveup", player.player().getName()).color(NamedTextColor.YELLOW),
                    Translation.component(p.player().locale(), "gameplay.info.ending.giveup-sub", player.player().getName()).color(NamedTextColor.GRAY)));
        }
    }

    public void draw() {
        for (PlayerData p : players) {
            p.player().showTitle(Title.title(Translation.component(p.player().locale(), "gameplay.ending.info.tie").color(NamedTextColor.YELLOW),
                    Translation.component(p.player().locale(), "gameplay.info.ending.tie-sub").color(NamedTextColor.GRAY)));
        }
    }

    // ========== TURN LOGIC ==========

    public void turn(@NotNull TurnData data) throws ImproperTurnException, TurnUnusableException {
        PlayerData player = data.player();
        Turn turn = data.turn();
        TurnData last = history.get(history.size() - 1);

        // Ensuring that the turn is currently usable and valid:
        if (switch (turn) {
            case PURPLE_WOOL -> {
                for (int i = 1; i <= 5; i++) {
                    if (history.get(history.size() - i).turn().function.name().contains("ATTACK"))
                        yield true;
                }
                yield false;
            }
            case MAGENTA_GLAZED_TERRACOTTA -> Objects.requireNonNull(data.block()).getRelative(((Directional) data.block()).getFacing()).equals(last.block());
            case LAVA -> warp.temperature != Warp.Temperature.WARM;
            default -> false;
        }) {
            throw new TurnUnusableException(data);
        }

        history.add(data);

        // Applying the effects as set in the enumeration:
        switch (turn.function) {
            case ATTACK -> attack(data);
            case COUNTER -> counter(data);
            case DEFENSE -> defense(data);
            case BUFF -> buffs(data);
            case COUNTERATTACK -> {
                counter(data);
                attack(data);
            }
            case COUNTERBUFF -> {
                counter(data);
                buffs(data);
            }
        }

        // Additional turn logic, if the basic things from the enumeration aren't enough:
        switch (turn) {
            case PURPLE_WOOL -> win(player);
            case CYAN_CARPET -> {
                if (player.getAttack().left()) {
                    counter(data);
                    attack(data);
                } else {
                    buffs(data);
                }
            }
            case SPONGE -> {
                world.setStorm(true);
                if (last.turn() == Turn.SPONGE) { // TODO: Replace with Turn.WATER!
                    new Buff.Luck(Buff.Target.YOU, 10).apply(player);
                    new Buff.Effect(Buff.Target.YOU, PotionEffectType.JUMP).apply(player);
                }
            }
            case LAVA -> {
                PlayerData opponent = getOpponent(player);
                opponent.specialEffects().add(new Burning(player, 3));
                opponent.player().sendMessage(Translation.component(opponent.player().locale(), "gameplay.effects.burning").color(NamedTextColor.RED));
            }
            case CAMPFIRE -> {
                if (last.turn() == Turn.CAMPFIRE) { // TODO: Replace with Turn.BEE_NEST!
                    new Buff.Luck(Buff.Target.YOU, 10).apply(player);
                    new Buff.ExtraTurn(Buff.Target.YOU).apply(player);
                }
            }
        }

        Player current = turning.player();
        Player opponent = getOpponent(turning).player();
        if (turning.postTurn()) {
            current.sendActionBar(Translation.component(current.locale(), "gameplay.info.extra-turn.turning").color(NamedTextColor.GREEN));
            opponent.sendActionBar(Translation.component(opponent.locale(), "gameplay.info.extra-turn.opponent", current.getName()).color(NamedTextColor.YELLOW));
        } else {
            opponent.sendActionBar(Translation.component(opponent.locale(), "gameplay.info.next-turn.turning").color(NamedTextColor.GREEN));
            current.sendActionBar(Translation.component(current.locale(), "gameplay.info.next-turn.opponent").color(NamedTextColor.GOLD));
            swapTurning();
        }
    }

    private static void counter(@NotNull TurnData data) throws ImproperTurnException {
        if (data.turn().countering.stream().noneMatch(f -> f.doCounter(data)))
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.WRONG_COUNTER, data);
    }

    private static void attack(@NotNull TurnData data) throws ImproperTurnException {
        if (data.player().getAttack().left())
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.ATTACK_WHILE_ATTACKED, data);

        PlayerData opponent = data.player().opponent();
        if (opponent.getDefense().left())
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.OPPONENT_DEFENDED, data);
        if (opponent.getAttack().left())
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.OPPONENT_ALREADY_ATTACKED, data);
        opponent.setAttack(Pair.of(true, new Attack(opponent)));
    }

    private static void defense(@NotNull TurnData data) throws ImproperTurnException {
        if (data.player().getDefense().left())
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.DEFENSE_WHILE_ATTACKED, data);
        if (data.player().opponent().getDefense().left())
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.OPPONENT_ALSO_DEFENDED, data);
        data.player().setDefense(Pair.of(true, new Defense(data.player(), 1, data.turn().name().endsWith("_WALL"))));
    }

    private static void buffs(@NotNull TurnData data) {
        data.turn().buffs.forEach(buff -> buff.apply(data.player()));
    }
}
