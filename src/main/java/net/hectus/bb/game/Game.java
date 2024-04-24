package net.hectus.bb.game;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.storing.Pair;
import com.marcpg.libpg.util.Randomizer;
import net.hectus.bb.event.custom.PlayerWarpEvent;
import net.hectus.bb.event.custom.TurnDoneEvent;
import net.hectus.bb.game.util.ImproperTurnException;
import net.hectus.bb.game.util.PlacementHandler;
import net.hectus.bb.game.util.TurnScheduler;
import net.hectus.bb.game.util.TurnUnusableException;
import net.hectus.bb.player.PlayerData;
import net.hectus.bb.shop.PreGameShop;
import net.hectus.bb.shop.ShopItemUtilities;
import net.hectus.bb.turn.Turn;
import net.hectus.bb.turn.TurnData;
import net.hectus.bb.turn.buff.Buff;
import net.hectus.bb.turn.effective.Attack;
import net.hectus.bb.turn.effective.Burning;
import net.hectus.bb.turn.effective.Defense;
import net.hectus.bb.util.Modifiers;
import net.hectus.bb.warp.Warp;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Game {
    public final Modifiers modifiers = new Modifiers();
    public final UUID uuid = UUID.randomUUID();
    public final PlacementHandler placementHandler = new PlacementHandler();

    private final List<TurnData> history = new ArrayList<>();
    private final List<PlayerData> players = new ArrayList<>();
    private final GameTicker ticker = new GameTicker(this);
    private final World world;

    public boolean started = false;
    private PlayerData turning;
    private Warp warp = Warp.DEFAULT;
    private List<Turn.ItemClass> allowed = Warp.DEFAULT.allowed;

    public Game(World world, Player challenger, Player challenged) {
        this.world = world;
        this.players.add(new PlayerData(challenger, this));
        this.players.add(new PlayerData(challenged, this));
        this.turning = this.players.get(1);
        this.players.forEach(PreGameShop::menu);
    }

    public List<TurnData> history() {
        return history;
    }

    public List<PlayerData> players() {
        return players;
    }

    public GameTicker ticker() {
        return ticker;
    }

    public PlayerData turning() {
        return turning;
    }

    public void swapTurning() {
        placementHandler.reset();
        turning = getOpponent(turning);
    }

    public PlayerData getOpponent(PlayerData playerData) {
        return players.get(players.indexOf(playerData) == 0 ? 1 : 0);
    }

    public Warp warp() {
        return warp;
    }

    public void warp(Warp warp) {
        if (modifiers.isEnabled("warp_100") || Randomizer.boolByChance(warp.chance)) {
            new PlayerWarpEvent(turning, this.warp, warp).callEvent();
            this.allowed = warp.allowed;
            this.warp = warp;
        } else {
            turning.player().sendMessage(Translation.component(turning.player().locale(), "gameplay.info.warp-unsuccessful.turning").color(NamedTextColor.RED));
            getOpponent(turning).player().sendMessage(Translation.component(turning.player().locale(), "gameplay.info.warp-unsuccessful.opponent", turning.player().getName()).color(NamedTextColor.YELLOW));
        }
    }

    public void startMainGame() {
        this.started = true;
        ticker.start();
    }

    public boolean hasStarted() {
        return started;
    }

    // ========== ENDING LOGIC ==========

    public void win(@NotNull PlayerData player) {
        Player p = player.player();
        p.showTitle(Title.title(Translation.component(p.locale(), "gameplay.info.ending.win").color(NamedTextColor.GREEN),
                Translation.component(p.locale(), "gameplay.info.ending.win-sub").color(NamedTextColor.GOLD)));

        Player o = getOpponent(player).player();
        o.showTitle(Title.title(Translation.component(o.locale(), "gameplay.info.ending.lose").color(NamedTextColor.RED),
                Translation.component(o.locale(), "gameplay.info.ending.lose-sub").color(NamedTextColor.GOLD)));

        ending();
    }

    public void lose(PlayerData player) {
        win(getOpponent(player));
    }

    public void giveUp(PlayerData player) {
        for (PlayerData p : players) {
            p.player().showTitle(Title.title(Translation.component(p.player().locale(), "gameplay.info.ending.giveup", player.player().getName()).color(NamedTextColor.YELLOW),
                    Translation.component(p.player().locale(), "gameplay.info.ending.giveup-sub", player.player().getName()).color(NamedTextColor.GRAY)));
        }
        ending();
    }

    public void draw() {
        for (PlayerData p : players) {
            p.player().showTitle(Title.title(Translation.component(p.player().locale(), "gameplay.ending.info.tie").color(NamedTextColor.YELLOW),
                    Translation.component(p.player().locale(), "gameplay.info.ending.tie-sub").color(NamedTextColor.GRAY)));
        }
        ending();
    }

    public void ending() {
        ticker.stop();
        GameManager.GAMES.remove(this);
        TurnScheduler.cancelAll(this);
    }

    // ========== TURN LOGIC ==========

    public void turn(@NotNull TurnData data) throws ImproperTurnException, TurnUnusableException {
        PlayerData player = data.player();
        PlayerData opp = getOpponent(player);
        Turn turn = data.turn();
        TurnData last = history.isEmpty() ? TurnData.EMPTY : history.get(history.size() - 1);

        if (!allowed.contains(turn.clazz))
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.WRONG_WARP, data);

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
            case FLOWER_AZURE_BLUET -> modifiers.isEnabled("azure_bluet_used");
            default -> false;
        }) {
            throw new TurnUnusableException(data);
        }

        history.add(data);

        // Applying the effects as set in the enumeration:
        switch (turn.function) {
            case ATTACK -> attack(data);
            case COUNTER -> counter(data, last);
            case DEFENSE -> defense(data);
            case BUFF -> buffs(data);
            case COUNTERATTACK -> {
                counter(data, last);
                attack(data);
            }
            case COUNTERBUFF -> {
                counter(data, last);
                buffs(data);
            }
        }

        // Additional turn logic, if the basic things from the enumeration aren't enough:
        switch (turn) {
            case PURPLE_WOOL -> win(player);
            case CYAN_CARPET -> {
                if (player.getAttack().left()) {
                    counter(data, last);
                    attack(data);
                } else {
                    buffs(data);
                }
            }
            case SPONGE -> {
                world.setStorm(true);
                if (last.turn() == Turn.WATER) {
                    new Buff.Luck(Buff.Target.YOU, 10).apply(player);
                    new Buff.Effect(Buff.Target.YOU, PotionEffectType.JUMP).apply(player);
                }
            }
            case LAVA -> {
                opp.specialEffects().add(new Burning(player, 3));
                opp.player().sendMessage(Translation.component(opp.player().locale(), "gameplay.effects.burning").color(NamedTextColor.RED));
            }
            case CAMPFIRE -> {
                if (last.turn() == Turn.BEE_NEST) {
                    new Buff.Luck(Buff.Target.YOU, 10).apply(player);
                    new Buff.ExtraTurn().apply(player);
                }
            }
            case POWDER_SNOW -> {
                opp.modifiers.enable("frozen");
                TurnScheduler.runTaskLater(this, "powder_snow", 3, () -> lose(opp));
            }
            case SEA_LANTERN -> {
                player.modifiers.enable("revive");
                player.player().sendMessage(Component.text("You now have a revive, good luck!"));
                player.player().getInventory().setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING));
            }
            case BEE_NEST -> {
                if (Randomizer.boolByChance(20)) {
                    Block block = Objects.requireNonNull(data.block());
                    block.getWorld().spawn(block.getLocation(), Bee.class);
                    player.addExtraTurns(1);
                    turn(new TurnData(player, Turn.BEE, null, null));
                }
            }
            case RED_CARPET -> {
                if (Randomizer.boolByChance(10)) {
                    player.inv().addItem(new ItemStack(Material.RED_WOOL));
                    player.player().playSound(player.player(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 1.0f);
                }
            }
            case PINK_BED -> {
                if (Randomizer.boolByChance(70))
                    player.player().clearActivePotionEffects();
            }
            case CHORUS_FRUIT -> {
                // TODO: Do this once maps are implemented!
            }
            case PIGLIN -> Objects.requireNonNull(data.entity()).addScoreboardTag(this.uuid.toString());
            case POLAR_BEAR -> player.modifiers.enable("no_jump");
            case AXOLOTL -> {
                if (data.entity() instanceof Axolotl axolotl) {
                    switch (axolotl.getVariant()) {
                        case LUCY -> warp(Randomizer.fromCollection(Arrays.stream(Warp.values())
                                .filter(w -> w.allowed.contains(Turn.ItemClass.HOT) && w != warp)
                                .toList()));
                        case WILD -> warp(Randomizer.fromCollection(Arrays.stream(Warp.values())
                                .filter(w -> w.allowed.contains(Turn.ItemClass.NATURE) && w != warp)
                                .toList()));
                        case GOLD -> warp(Randomizer.fromCollection(Arrays.stream(Warp.values())
                                .filter(w -> w.allowed.contains(Turn.ItemClass.SUPERNATURAL) && w != warp)
                                .toList()));
                        case CYAN -> warp(Randomizer.fromCollection(Arrays.stream(Warp.values())
                                .filter(w -> w.allowed.contains(Turn.ItemClass.WATER_CLASS) && w != warp)
                                .toList()));
                        case BLUE -> win(player);
                    }
                }
            }
            case BEE -> {
                if (last.turn().name().startsWith("FLOWER_") && last.turn().buffs != null) {
                    last.turn().buffs.forEach(b -> b.revert(player));
                }
            }
            case SHEEP -> {
                if (data.entity() instanceof Sheep sheep) {
                    switch (Objects.requireNonNull(sheep.getColor())) {
                        case WHITE -> {
                            if (allowed.contains(Turn.ItemClass.COLD))
                                new Buff.ExtraTurn().apply(player);
                        }
                        case PINK -> win(player);
                        case GRAY -> new Buff.Luck(Buff.Target.YOU, 25).apply(player);
                        case LIGHT_GRAY -> new Buff.ExtraTurn().apply(player);
                        case BLUE -> {
                            if (data.player().getAttack().left())
                                Objects.requireNonNull(data.player().getAttack().right()).decrement();
                        }
                        case BROWN -> player.inv().addItem(ShopItemUtilities.item(player.player().locale(), randomItem()));
                        case BLACK -> new Buff.Luck(Buff.Target.OPPONENT, -15);
                    }
                }
            }
            case EVOKER -> {
                if (player.getAttack().left()) {
                    counter(data, last);
                    buffs(data);
                } else {
                    Vex vex = player.player().getWorld().spawn(player.player().getLocation().add(0, 2, 0), Vex.class);
                    vex.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, -1, 2));
                    vex.getActiveItem().addEnchantment(Enchantment.KNOCKBACK, 2);
                }
            }
            case DIRT, FLOWER_POT -> new Buff.ExtraTurn().apply(player);
            case FLOWER_POPPY -> {
                List<ItemStack> yourInv = List.copyOf(player.inv().getContents());
                player.inv().setContents(opp.inv().getContents());
                opp.inv().setContents(yourInv);
            }
            case FLOWER_BLUE_ORCHID -> modifiers.enable("warp_100");
            case FLOWER_ALLIUM -> opp.inv().removeItemInShop(Randomizer.fromCollection(opp.inv().getContents()));
            case FLOWER_AZURE_BLUET -> {
                modifiers.enable("azure_bluet_used");
                new Buff.Luck(Buff.Target.YOU, 20).apply(player);
            }
            case FLOWER_RED_TULIP -> allowed.add(Turn.ItemClass.REDSTONE);
            case FLOWER_ORANGE_TULIP -> allowed.add(Turn.ItemClass.HOT);
            case FLOWER_WHITE_TULIP -> allowed.add(Turn.ItemClass.COLD);
            case FLOWER_PINK_TULIP -> allowed.add(Turn.ItemClass.SUPERNATURAL);
            case FLOWER_CORNFLOWER -> {
                allowed.add(Turn.ItemClass.WATER_CLASS);
                world.setStorm(true);
            }
            case FLOWER_WITHER_ROSE -> allowed.remove(Turn.ItemClass.SUPERNATURAL);
            case FLOWER_SUNFLOWER -> world.setStorm(false);
            case FLOWER_SPORE_BLOSSOM -> player.modifiers.enable("always_move");
            case NOTE_BLOCK -> {
                if (data.block() instanceof NoteBlock noteBlock) {
                    new Buff.Luck(Buff.Target.YOU, switch (noteBlock.getInstrument()) {
                        case PIANO -> -5;
                        case BASS_DRUM, BASS_GUITAR -> 5;
                        case STICKS -> 10;
                        case DIDGERIDOO -> 15;
                        case GUITAR, COW_BELL, BANJO -> 20;
                        case BELL, CHIME -> 35;
                        default -> 0;
                    }).apply(player);
                }
            }
        }

        done(data);
    }

    public void done(@NotNull TurnData data) {
        if (data.player().getAttack().left()) {
            if (data.player().modifiers.isEnabled("revive")) {
                data.player().player().playSound(data.player().player(), Sound.ITEM_TOTEM_USE, 1.5f, 1.0f);
                data.player().modifiers.disable("revive");
            } else {
                lose(data.player());
            }
        }

        new TurnDoneEvent(data).callEvent();

        Player current = data.player().player();
        Player opponent = data.player().opponent().player();
        if (turning.extraTurn()) {
            current.sendActionBar(Translation.component(current.locale(), "gameplay.info.extra-turn.turning").color(NamedTextColor.GREEN));
            opponent.sendActionBar(Translation.component(opponent.locale(), "gameplay.info.extra-turn.opponent", current.getName()).color(NamedTextColor.YELLOW));
        } else {
            opponent.sendActionBar(Translation.component(opponent.locale(), "gameplay.info.next-turn.turning").color(NamedTextColor.GREEN));
            current.sendActionBar(Translation.component(current.locale(), "gameplay.info.next-turn.opponent").color(NamedTextColor.GOLD));
            swapTurning();
        }
    }

    private void counter(@NotNull TurnData data, @NotNull TurnData last) throws ImproperTurnException {
        if (last.turn() == Turn.POWDER_SNOW) {
            data.player().modifiers.disable("frozen");
            TurnScheduler.cancel(this, "powder_snow");
        } else if (last.turn() == Turn.LAVA) {
            data.player().specialEffects().removeIf(tc -> tc instanceof Burning);
        }

        if (data.turn().countering.stream().noneMatch(f -> f.doCounter(last)))
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.WRONG_COUNTER, data);
        if (data.player().getAttack().left()) {
            Objects.requireNonNull(data.player().getAttack().right()).decrement();
        }
    }

    private void attack(@NotNull TurnData data) throws ImproperTurnException {
        if (data.player().getAttack().left())
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.ATTACK_WHILE_ATTACKED, data);

        PlayerData opponent = data.player().opponent();
        if (data.turn() != Turn.PHANTOM && opponent.getDefense().left())
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.OPPONENT_DEFENDED, data);
        if (opponent.getAttack().left())
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.OPPONENT_ALREADY_ATTACKED, data);
        opponent.setAttack(Pair.of(true, new Attack(opponent)));
    }

    private void defense(@NotNull TurnData data) throws ImproperTurnException {
        if (data.player().getDefense().left())
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.DEFENSE_WHILE_ATTACKED, data);
        if (data.player().opponent().getDefense().left())
            throw new ImproperTurnException(ImproperTurnException.ImproperTurnCause.OPPONENT_ALSO_DEFENDED, data);
        data.player().setDefense(Pair.of(true, new Defense(data.player(), 1, data.turn().name().endsWith("_WALL"))));
    }

    private void buffs(@NotNull TurnData data) {
        if (data.turn().buffs == null) return;
        data.turn().buffs.forEach(buff -> buff.apply(data.player()));
    }

    private static Material randomItem() {
        return Randomizer.fromCollection(Randomizer.fromArray(Turn.values()).items);
    }
}
