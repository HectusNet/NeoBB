package net.hectus.bb.turn;

import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.player.PlayerInv;
import net.hectus.bb.turn.buff.Buff;
import net.hectus.bb.turn.buff.ChanceBuff;
import net.hectus.bb.turn.counter.CounterFilter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Sheep;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

import static net.hectus.bb.player.PlayerInv.Hotbar.*;
import static net.hectus.bb.turn.Turn.ItemClass.*;
import static net.hectus.bb.turn.Turn.ItemFunction.*;
import static net.hectus.bb.turn.Turn.ItemType.*;

public enum Turn {
    PURPLE_WOOL(false, null, null, OVERTIME, BLOCK, ATTACK, NEUTRAL, Material.PURPLE_WOOL),
    SPRUCE_TRAPDOOR(false, null, List.of(NEUTRAL, WATER_CLASS, NATURE), BOTH, BLOCK, COUNTER, NEUTRAL, Material.SPRUCE_TRAPDOOR),
    IRON_TRAPDOOR(false, null, List.of(NEUTRAL, WATER_CLASS, REDSTONE), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.IRON_TRAPDOOR),
    CAULDRON(false, null, null, OVERTIME, BLOCK, ATTACK, NEUTRAL, Material.CAULDRON),
    GOLD_BLOCK(false, null, List.of(NEUTRAL, HOT, NATURE), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.GOLD_BLOCK),
    BLACK_WOOL(false, List.of(new Buff.Effect(Buff.Target.BOTH, PotionEffectType.BLINDNESS)), List.of(COLD, SUPERNATURAL), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.BLACK_WOOL),
    SCULK(false, List.of(new Buff.Luck(Buff.Target.OPPONENT, -10), new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.DARKNESS)), List.of(NEUTRAL, SUPERNATURAL), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.SCULK),
    GREEN_CARPET(false, null, List.of(new EndingCounter("_WOOL")), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.GREEN_CARPET),
    STONECUTTER(false, List.of(new Buff.ExtraTurn()), List.of(new WallCounter()), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.STONECUTTER),
    MAGENTA_GLAZED_TERRACOTTA(true, null, List.of(NEUTRAL, HOT, COLD, WATER_CLASS, NATURE), BOTH, BLOCK, COUNTERATTACK, NEUTRAL, Material.MAGENTA_GLAZED_TERRACOTTA),
    CYAN_CARPET(false, List.of(new Buff.ExtraTurn()), List.of(REDSTONE, SUPERNATURAL), BOTH, BLOCK, ItemFunction.OTHER, NEUTRAL, Material.CYAN_CARPET),
    SPONGE(true, List.of(new Buff.ExtraTurn(), new Buff.Luck(Buff.Target.YOU, 5)), List.of(NEUTRAL, WATER_CLASS, SUPERNATURAL), BOTH, BLOCK, COUNTERBUFF, NEUTRAL, Material.SPONGE),
    MAGMA_BLOCK(true, null, null, BOTH, BLOCK, ATTACK, HOT, Material.MAGMA_BLOCK),
    NETHERRACK(false, null, List.of(HOT, COLD, WATER_CLASS), BOTH, BLOCK, COUNTERATTACK, HOT, Material.NETHERRACK),
    LAVA(true, null, null, BOTH, BLOCK, BUFF, HOT, Material.LAVA_BUCKET),
    FIRE(false, List.of(new Buff.Effect(Buff.Target.BOTH, PotionEffectType.BLINDNESS), new Buff.Effect(Buff.Target.BOTH, PotionEffectType.GLOWING)), List.of(COLD, REDSTONE, NATURE), BOTH, BLOCK, COUNTER, HOT, Material.FLINT_AND_STEEL),
    ORANGE_WOOL(false, null, null, BOTH, BLOCK, COUNTERATTACK, HOT, Material.ORANGE_WOOL),
    CAMPFIRE(false, null, List.of(NEUTRAL, HOT, COLD), BOTH, BLOCK, COUNTERATTACK, HOT, Material.CAMPFIRE),
    RESPAWN_ANCHOR(true, List.of(new Buff.Teleport(Buff.Target.YOU), new ChanceBuff(new Buff.ExtraTurn(), 50)), null, BOTH, BLOCK, BUFF, HOT, Material.RESPAWN_ANCHOR),
    PACKED_ICE(false, null, null, BOTH, BLOCK, ATTACK, COLD, Material.PACKED_ICE),
    BLUE_ICE(false, null, List.of(COLD, WATER_CLASS, SUPERNATURAL), BOTH, BLOCK, COUNTER, COLD, Material.BLUE_ICE),
    SPRUCE_LEAVES(false, null, List.of(NEUTRAL, NATURE), BOTH, BLOCK, COUNTERATTACK, COLD, Material.SPRUCE_LEAVES),
    LIGHT_BLUE_WOOL(false, null, null, BOTH, BLOCK, ATTACK, COLD, Material.LIGHT_BLUE_WOOL),
    WHITE_WOOL(false, List.of(new Buff.Effect(Buff.Target.BOTH, PotionEffectType.SLOW, 255)), List.of(NEUTRAL, WATER_CLASS, REDSTONE, SUPERNATURAL), BOTH, BLOCK, COUNTERBUFF, COLD, Material.WHITE_WOOL),
    POWDER_SNOW(false, null, null, BOTH, BLOCK, ATTACK, COLD, Material.POWDER_SNOW_BUCKET),
    BRAIN_CORAL_BLOCK(false, null, List.of(REDSTONE), BOTH, BLOCK, COUNTERATTACK, WATER_CLASS, Material.BRAIN_CORAL_BLOCK),
    HORN_CORAL(false, null, List.of(NEUTRAL, NATURE, WATER_CLASS), BOTH, BLOCK, COUNTERATTACK, WATER_CLASS, Material.HORN_CORAL),
    FIRE_CORAL(false, null, List.of(HOT, REDSTONE), BOTH, BLOCK, COUNTERATTACK, WATER_CLASS, Material.FIRE_CORAL),
    FIRE_CORAL_FAN(false, List.of(new ChanceBuff(new Buff.ExtraTurn(), 80)), List.of(HOT, REDSTONE), BOTH, BLOCK, COUNTERBUFF, WATER_CLASS, Material.FIRE_CORAL_FAN),
    SEA_LANTERN(false, null, null, BOTH, BLOCK, BUFF, WATER_CLASS, Material.SEA_LANTERN),
    WATER(false, null, List.of(new WaterLoggableCounter()), BOTH, BLOCK, COUNTER, WATER_CLASS, Material.WATER_BUCKET),
    DRIED_KELP_BLOCK(false, List.of(new Buff.Effect(Buff.Target.YOU, PotionEffectType.JUMP, 2)), null, BOTH, BLOCK, BUFF, WATER_CLASS, Material.DRIED_KELP_BLOCK),
    VERDANT_FROGLIGHT(false, null, List.of(SUPERNATURAL), BOTH, BLOCK, COUNTERATTACK, COLD, Material.VERDANT_FROGLIGHT),
    BEE_NEST(false, null, null, BOTH, BLOCK, ATTACK, NATURE, Material.BEE_NEST),
    HONEY_BLOCK(false, null, List.of(NATURE, WATER_CLASS, REDSTONE), BOTH, BLOCK, COUNTER, NATURE, Material.HONEY_BLOCK),
    GREEN_WOOL(false, null, List.of(NATURE, HOT, COLD), BOTH, BLOCK, COUNTERATTACK, NATURE, Material.GREEN_WOOL),
    MANGROVE_ROOTS(false, null, List.of(NEUTRAL, WATER_CLASS, NATURE), BOTH, BLOCK, COUNTER, NATURE, Material.MANGROVE_ROOTS),
    COMPOSTER(true, null, null, BOTH, BLOCK, ATTACK, NATURE, Material.COMPOSTER),
    HAY_BLOCK(false, null, List.of(COLD, WATER_CLASS), BOTH, BLOCK, COUNTERATTACK, NATURE, Material.HAY_BLOCK),
    OAK_STAIRS(false, List.of(new Buff.ExtraTurn()), List.of(HOT, NATURE, SUPERNATURAL), BOTH, BLOCK, COUNTERBUFF, NATURE, Material.OAK_STAIRS),
    // LEVER(true, null, List.of(NEUTRAL, REDSTONE), BOTH, BLOCK, COUNTER, REDSTONE, Material.LEVER),
    FENCE_GATE(false, null, List.of(NEUTRAL, HOT), BOTH, BLOCK, COUNTERATTACK, REDSTONE, Material.OAK_FENCE_GATE),
    PISTON(true, null, List.of(last -> true), BOTH, BLOCK, COUNTER, REDSTONE, Material.PISTON),
    REPEATER(false, null, List.of(NEUTRAL, COLD, NATURE), BOTH, BLOCK, COUNTERATTACK, REDSTONE, Material.REPEATER),
    LIGHTNING_ROD(false, null, List.of(NEUTRAL, HOT, REDSTONE, SUPERNATURAL), BOTH, BLOCK, COUNTERATTACK, REDSTONE, Material.LIGHTNING_ROD),
    // OBSERVER(false, null, null, BOTH, BLOCK, EVENT, REDSTONE, Material.OBSERVER),
    // WOOD_BUTTON(false, null, List.of(HOT, COLD, WATER), BOTH, BLOCK, COUNTER, REDSTONE, Material.OAK_BUTTON, Material.BIRCH_BUTTON, Material.DARK_OAK_BUTTON, Material.BAMBOO_BUTTON, Material.ACACIA_BUTTON, Material.CHERRY_BUTTON),
    // STONE_BUTTON(false, null, List.of(NATURE, REDSTONE, SUPERNATURAL), BOTH, BLOCK, COUNTERATTACK, REDSTONE, Material.STONE_BUTTON),
    RED_CARPET(false, List.of(new Buff.ExtraTurn()), List.of(NEUTRAL, COLD, REDSTONE), BOTH, BLOCK, COUNTERBUFF, REDSTONE, Material.RED_CARPET),
    RED_BED(false, null, List.of(NEUTRAL, COLD, SUPERNATURAL), BOTH, BLOCK, COUNTERATTACK, SUPERNATURAL, Material.RED_BED),
    PINK_BED(false, null, null, BOTH, BLOCK, BUFF, SUPERNATURAL, Material.PINK_BED),
    GREEN_BED(false, null, List.of(NEUTRAL, NATURE), BOTH, BLOCK, COUNTERATTACK, SUPERNATURAL, Material.GREEN_BED),
    BLUE_BED(false, List.of(new Buff.Luck(Buff.Target.YOU, 5)), List.of(COLD, WATER_CLASS, NATURE, SUPERNATURAL), BOTH, BLOCK, COUNTERBUFF, SUPERNATURAL, Material.BLUE_BED),
    DRAGON_HEAD(false, List.of(new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.BLINDNESS), new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.SLOW), new Buff.Luck(Buff.Target.OPPONENT, -20)), null, BOTH, BLOCK, BUFF, SUPERNATURAL, Material.DRAGON_HEAD),
    SOUL_SAND(false, null, null, BOTH, BLOCK, ATTACK, SUPERNATURAL, Material.SOUL_SAND),
    CHORUS_FRUIT(false, null, null, BOTH, ITEM, EVENT, NEUTRAL, Material.CHORUS_FRUIT),
    IRON_SHOVEL(true, List.of(new Buff.ExtraTurn()), List.of(last -> last.turn() == CAULDRON, HOT), BOTH, ITEM, COUNTERBUFF, NEUTRAL, Material.IRON_SHOVEL),
    BLAZE(false, null, null, BOTH, MOB, ATTACK, HOT, Material.BLAZE_SPAWN_EGG),
    PIGLIN(true, List.of(new Buff.Effect(Buff.Target.YOU, PotionEffectType.BLINDNESS), new Buff.Effect(Buff.Target.YOU, PotionEffectType.SLOW), new Buff.Luck(Buff.Target.YOU, -30)), null, REGULAR, ItemType.OTHER, EVENT, HOT, Material.PIGLIN_SPAWN_EGG),
    POLAR_BEAR(false, List.of(new Buff.Effect(Buff.Target.OPPONENT, PotionEffectType.SPEED, 2)), null, BOTH, MOB, BUFF, COLD, Material.POLAR_BEAR_SPAWN_EGG),
    AXOLOTL(false, List.of(new Buff.Luck(Buff.Target.OPPONENT, -10), new Buff.Luck(Buff.Target.OPPONENT, 10)), null, OVERTIME, MOB, BUFF, WATER_CLASS, Material.AXOLOTL_SPAWN_EGG),
    BEE(false, null, null, BOTH, MOB, BUFF, NATURE, Material.BEE_SPAWN_EGG),
    SHEEP(false, List.of(new Buff.Luck(Buff.Target.YOU, 5)), null, BOTH, MOB, BUFF, SUPERNATURAL, Material.SHEEP_SPAWN_EGG),
    PHANTOM(false, null, null, BOTH, MOB, ATTACK, SUPERNATURAL, Material.PHANTOM_SPAWN_EGG),
    EVOKER(false, List.of(new Buff.Luck(Buff.Target.YOU, 20), new Buff.ExtraTurn()), List.of(last -> last.entity() instanceof Sheep sheep && sheep.getColor() == DyeColor.BLUE), BOTH, MOB, ItemFunction.OTHER, SUPERNATURAL, Material.EVOKER_SPAWN_EGG),
    OAK_DOOR_TURTLING(true, null, null, BOTH, STRUCTURE, DEFENSE, NEUTRAL, Material.OAK_DOOR),
    PUMPKIN_WALL(true, null, null, BOTH, STRUCTURE, BUFF, NATURE, Material.CARVED_PUMPKIN),
    DAYLIGHT_SENSOR_LINE(true, null, List.of(HOT, NATURE, WATER_CLASS, REDSTONE), BOTH, STRUCTURE, COUNTERBUFF, REDSTONE, Material.DAYLIGHT_DETECTOR),
    NOTE_BLOCK(true, List.of(new Buff.Luck(Buff.Target.YOU, 10)), List.of(last -> last.block() != null && last.block().isSolid()), BOTH, BLOCK, COUNTERBUFF, REDSTONE, Material.NOTE_BLOCK),
    // ========== FLOWERS ==========
    DIRT(false, null, null, BOTH, BLOCK, ItemFunction.OTHER, NATURE, Material.DIRT),
    FLOWER_POT(false, null, null, BOTH, BLOCK, ItemFunction.OTHER, NATURE, Material.FLOWER_POT),
    FLOWER_POPPY(false, null, null, BOTH, BLOCK, BUFF, NATURE, Material.POPPY),
    FLOWER_BLUE_ORCHID(false, null, null, BOTH, BLOCK, BUFF, NATURE, Material.BLUE_ORCHID),
    FLOWER_ALLIUM(false, null, null, BOTH, BLOCK, BUFF, NATURE, Material.ALLIUM),
    FLOWER_AZURE_BLUET(false, List.of(new Buff.Luck(Buff.Target.YOU, 20)), null, BOTH, BLOCK, BUFF, NATURE, Material.AZURE_BLUET),
    FLOWER_RED_TULIP(false, List.of(new Buff.ExtraTurn()), null, BOTH, BLOCK, BUFF, NATURE, Material.RED_TULIP),
    FLOWER_ORANGE_TULIP(false, List.of(new Buff.ExtraTurn()), null, BOTH, BLOCK, BUFF, NATURE, Material.ORANGE_TULIP),
    FLOWER_WHITE_TULIP(false, List.of(new Buff.ExtraTurn()), null, BOTH, BLOCK, BUFF, NATURE, Material.WHITE_TULIP),
    FLOWER_PINK_TULIP(false, List.of(new Buff.ExtraTurn()), null, BOTH, BLOCK, BUFF, NATURE, Material.PINK_TULIP),
    FLOWER_CORNFLOWER(false, List.of(new Buff.Effect(Buff.Target.BOTH, PotionEffectType.DARKNESS)), null, BOTH, BLOCK, BUFF, NATURE, Material.CORNFLOWER),
    FLOWER_OXEYE_DAISY(false, List.of(new ChanceBuff(new Buff.ExtraTurn(), 25)), null, BOTH, BLOCK, BUFF, NATURE, Material.OXEYE_DAISY),
    FLOWER_WITHER_ROSE(false, List.of(new Buff.Luck(Buff.Target.OPPONENT, -15)), null, BOTH, BLOCK, BUFF, NATURE, Material.WITHER_ROSE),
    FLOWER_SUNFLOWER(false, List.of(new Buff.Luck(Buff.Target.YOU, 10), new Buff.Effect(Buff.Target.BOTH, PotionEffectType.SPEED, 1)), null, BOTH, BLOCK, BUFF, NATURE, Material.SUNFLOWER),
    FLOWER_OAK_SAPLING(false, null, null, BOTH, BLOCK, BUFF, NATURE, Material.OAK_SAPLING),
    FLOWER_SPORE_BLOSSOM(false, null, null, BOTH, ItemType.OTHER, BUFF, NATURE, Material.SPORE_BLOSSOM);

    public final boolean usage;
    public final List<Buff> buffs;
    public final List<CounterFilter> countering;
    public final PlayerInv.Hotbar hotbar;
    public final ItemType type;
    public final ItemFunction function;
    public final ItemClass clazz;
    public final List<Material> items;

    // Keep in mind that the ItemType is also used for determining what event is used to call the turn!
    Turn(boolean usage, @Nullable List<Buff> buffs, @Nullable List<CounterFilter> countering, PlayerInv.Hotbar hotbar,
         ItemType type, ItemFunction function, ItemClass clazz, Material... items) {
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
        NEUTRAL, HOT, COLD, WATER_CLASS, NATURE, REDSTONE, SUPERNATURAL, OTHER;

        public @NotNull Component translate(Locale l) {
            return Translation.component(l, "info.class." + name().toLowerCase()).color(switch (this) {
                case HOT -> NamedTextColor.GOLD;
                case COLD -> NamedTextColor.WHITE;
                case WATER_CLASS -> NamedTextColor.BLUE;
                case NATURE -> NamedTextColor.GREEN;
                case REDSTONE -> NamedTextColor.RED;
                case SUPERNATURAL -> NamedTextColor.YELLOW;
                default -> NamedTextColor.GRAY;
            });
        }

        @Override
        public boolean doCounter(@NotNull TurnData last) {
            return last.turn().clazz == this;
        }
    }
}
