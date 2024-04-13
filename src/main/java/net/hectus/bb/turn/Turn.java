package net.hectus.bb.turn;

import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.player.PlayerInv;
import net.hectus.bb.turn.buff.Buff;
import net.hectus.bb.turn.counter.CounterFilter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

import static net.hectus.bb.player.PlayerInv.Hotbar.BOTH;
import static net.hectus.bb.player.PlayerInv.Hotbar.OVERTIME;
import static net.hectus.bb.turn.Turn.ItemClass.*;
import static net.hectus.bb.turn.Turn.ItemFunction.*;
import static net.hectus.bb.turn.Turn.ItemType.BLOCK;

public enum Turn {
    PURPLE_WOOL(false, null, null, OVERTIME, BLOCK, ATTACK, NEUTRAL, Material.PURPLE_WOOL),
    SPRUCE_TRAPDOOR(false, null, List.of(NEUTRAL, WATER, NATURE), BOTH, BLOCK, COUNTER, NEUTRAL, Material.SPRUCE_TRAPDOOR),
    IRON_TRAPDOOR(false, null, List.of(NEUTRAL, WATER, REDSTONE), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.IRON_TRAPDOOR),
    CAULDRON(false, null, null, OVERTIME, BLOCK, ATTACK, NEUTRAL, Material.CAULDRON),
    GOLD_BLOCK(false, null, List.of(NEUTRAL, HOT, NATURE), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.GOLD_BLOCK),
    BLACK_WOOL(false, List.of(new Buff.Effect(Buff.Target.BOTH, PotionEffectType.BLINDNESS)), List.of(COLD, SUPERNATURAL), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.BLACK_WOOL),
    SCULK(false, List.of(new Buff.Luck(Buff.Target.OPPONENT, -10), new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.DARKNESS)), List.of(NEUTRAL, SUPERNATURAL), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.SCULK),
    GREEN_CARPET(false, null, List.of(new EndingCounter("_WOOL")), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.GREEN_CARPET),
    STONECUTTER(false, List.of(new Buff.ExtraTurn(Buff.Target.YOU)), List.of(new WallCounter()), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.STONECUTTER),
    MAGENTA_GLAZED_TERRACOTTA(true, null, List.of(NEUTRAL, HOT, COLD, WATER, NATURE), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.MAGENTA_GLAZED_TERRACOTTA),
    CYAN_CARPET(false, List.of(new Buff.ExtraTurn(Buff.Target.YOU)), List.of(REDSTONE, SUPERNATURAL), BOTH, BLOCK, ItemFunction.OTHER, NEUTRAL, Material.CYAN_CARPET),
    SPONGE(true, List.of(new Buff.ExtraTurn(Buff.Target.YOU), new Buff.Luck(Buff.Target.YOU, 5)), List.of(NEUTRAL, WATER, SUPERNATURAL), BOTH, BLOCK, COUNTERBUFF, NEUTRAL, Material.SPONGE),
    MAGMA_BLOCK(true, null, null, BOTH, BLOCK, ATTACK, HOT, Material.MAGMA_BLOCK),
    NETHERRACK(false, null, List.of(HOT, COLD, WATER), BOTH, BLOCK, COUNTERATTACK, HOT, Material.NETHERRACK),
    LAVA(true, null, null, BOTH, BLOCK, BUFF, HOT, Material.LAVA_BUCKET),
    FIRE(false, List.of(new Buff.Effect(Buff.Target.BOTH, PotionEffectType.BLINDNESS), new Buff.Effect(Buff.Target.BOTH, PotionEffectType.GLOWING)), List.of(COLD, REDSTONE, NATURE), BOTH, BLOCK, COUNTER, HOT, Material.FLINT_AND_STEEL),
    ORANGE_WOOL(false, null, null, BOTH, BLOCK, COUNTERATTACK, HOT, Material.ORANGE_WOOL),
    CAMPFIRE(false, null, List.of(NEUTRAL, HOT, COLD), BOTH, BLOCK, COUNTERATTACK, HOT, Material.CAMPFIRE);

    public final boolean usage;
    public final List<Buff> buffs;
    public final List<CounterFilter> countering;
    public final PlayerInv.Hotbar hotbar;
    public final ItemType type;
    public final ItemFunction function;
    public final ItemClass clazz;
    public final List<Material> items;

    Turn(boolean usage, @Nullable List<Buff> buffs, @Nullable List<CounterFilter> countering, PlayerInv.Hotbar hotbar, ItemType type, ItemFunction function, ItemClass clazz, Material... items) {
        this.usage = usage;
        this.buffs = buffs;
        this.countering = countering;
        this.hotbar = hotbar;
        this.type = type;
        this.function = function;
        this.clazz = clazz;
        this.items = List.of(items);
    }

    public enum ItemType {
        BLOCK, ITEM, THROWABLE, MOB, STRUCTURE, OTHER;

        public @NotNull Component translate(Locale l) {
            return Translation.component(l, "info.type." + name().toLowerCase()).color(switch (this) {
                case BLOCK -> NamedTextColor.BLUE;
                case ITEM -> NamedTextColor.RED;
                case THROWABLE -> NamedTextColor.LIGHT_PURPLE;
                case MOB -> NamedTextColor.GREEN;
                case STRUCTURE -> NamedTextColor.YELLOW;
                case OTHER -> NamedTextColor.GRAY;
            });
        }
    }

    public enum ItemFunction {
        ATTACK, COUNTER, COUNTERATTACK, COUNTERBUFF, BUFF, DEFENSE, EVENT, OTHER;

        public @NotNull Component translate(Locale l) {
            return Translation.component(l, "info.function." + name().toLowerCase()).color(switch (this) {
                case ATTACK -> NamedTextColor.RED;
                case COUNTER -> NamedTextColor.YELLOW;
                case COUNTERATTACK, COUNTERBUFF -> NamedTextColor.GOLD;
                case BUFF -> NamedTextColor.GREEN;
                case DEFENSE -> NamedTextColor.BLUE;
                case EVENT -> NamedTextColor.LIGHT_PURPLE;
                case OTHER -> NamedTextColor.GRAY;
            });
        }
    }

    public enum ItemClass implements CounterFilter {
        NEUTRAL, HOT, COLD, WATER, NATURE, REDSTONE, SUPERNATURAL, OTHER;

        public @NotNull Component translate(Locale l) {
            return Translation.component(l, "info.class." + name().toLowerCase()).color(switch (this) {
                case HOT -> NamedTextColor.GOLD;
                case COLD -> NamedTextColor.WHITE;
                case WATER -> NamedTextColor.BLUE;
                case NATURE -> NamedTextColor.GREEN;
                case REDSTONE -> NamedTextColor.RED;
                case SUPERNATURAL -> NamedTextColor.YELLOW;
                default -> NamedTextColor.GRAY;
            });
        }

        @Override
        public boolean doCounter(@NotNull TurnData turn) {
            return turn.turn().clazz == this;
        }
    }
}
