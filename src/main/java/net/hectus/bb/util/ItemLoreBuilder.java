package net.hectus.bb.util;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.text.Formatter;
import net.hectus.bb.player.PlayerInv;
import net.hectus.bb.turn.Turn;
import net.hectus.bb.turn.buff.Buff;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public final class ItemLoreBuilder {
    private static final Component SEPARATOR = Component.text("                    ", NamedTextColor.DARK_GRAY);

    private final Material item;
    private int cost;

    private PlayerInv.Hotbar hotbar;
    private Turn.ItemType type;
    private Turn.ItemFunction function;
    private Turn.ItemClass clazz;

    private boolean hasUsage;

    private List<Buff> buffs = new ArrayList<>();

    public ItemLoreBuilder(Material item, int cost, PlayerInv.Hotbar hotbar, Turn.ItemType type, Turn.ItemFunction function, Turn.ItemClass clazz) {
        this.item = item;
        this.cost = cost;
        this.hotbar = hotbar;
        this.type = type;
        this.function = function;
        this.clazz = clazz;
    }

    public ItemLoreBuilder(Material item, int cost) {
        this(item, cost, PlayerInv.Hotbar.BOTH, Turn.ItemType.OTHER, Turn.ItemFunction.OTHER, Turn.ItemClass.OTHER);
    }

    public ItemLoreBuilder(Material item) {
        this(item, 0);
    }

    public ItemLoreBuilder cost(int cost) {
        this.cost = cost;
        return this;
    }

    public ItemLoreBuilder type(Turn.ItemType type) {
        this.type = type;
        return this;
    }

    public ItemLoreBuilder function(Turn.ItemFunction function) {
        this.function = function;
        return this;
    }

    public ItemLoreBuilder clazz(Turn.ItemClass clazz) {
        this.clazz = clazz;
        return this;
    }

    public ItemLoreBuilder hotbar(PlayerInv.Hotbar hotbar) {
        this.hotbar = hotbar;
        return this;
    }

    public ItemLoreBuilder setUsageInfo(boolean hasUsage) {
        this.hasUsage = hasUsage;
        return this;
    }

    public ItemLoreBuilder enableUsageInfo() {
        return setUsageInfo(true);
    }

    public ItemLoreBuilder buffs(List<Buff> buffs) {
        this.buffs = buffs;
        return this;
    }

    public ItemLoreBuilder addBuff(Buff buff) {
        this.buffs.add(buff);
        return this;
    }

    public ItemLoreBuilder removeBuff(Buff buff) {
        this.buffs.remove(buff);
        return this;
    }

    public @NotNull List<Component> build(Locale l) {
        List<Component> lore = new ArrayList<>();

        lore.add(SEPARATOR);
        lore.add(key(l, "item-lore.cost.key", "₪").append(Translation.component(l, "item-lore.cost.value", cost).color(NamedTextColor.GOLD)));
        lore.add(key(l, "info.hotbar.hotbar", "❖").append(hotbar.getTranslatedComponent(l)));
        lore.add(SEPARATOR);
        lore.add(key(l, "info.type.type", "❖").append(type.translate(l)));
        lore.add(key(l, "info.function.function", "❖").append(function.translate(l)));
        lore.add(key(l, "info.class.class", "❖").append(clazz.translate(l)));
        lore.add(SEPARATOR);
        lore.add(key(l, "item-lore.description", "❖"));
        lore.addAll(longText(l, "description"));
        if (hasUsage) {
            lore.add(SEPARATOR);
            lore.add(key(l, "item-lore.usage", "➽"));
            lore.addAll(longText(l, "usage"));
        }
        if (buffs != null && !buffs.isEmpty()) {
            lore.add(SEPARATOR);
            lore.add(key(l, "item-lore.buffs", "↑"));
            lore.addAll(buffs.stream().map(b -> b.line(l)).toList());
        }

        return lore;
    }

    private @NotNull Component key(Locale l, String key, String icon) {
        return Component.text(icon + " ").append(Translation.component(l, key)).append(Component.text(": "))
                .color(NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    private @NotNull List<? extends Component> longText(Locale l, String type) {
        String text = Translation.string(l, type + "." + item.name().toLowerCase());
        return Formatter.lineWrap(text, 50).stream()
                .map(s -> Component.text("   | " + s, NamedTextColor.BLUE)
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .toList();
    }

    public static ItemLoreBuilder of(Material m, int cost, @NotNull Turn t) {
        return new ItemLoreBuilder(m, cost, t.hotbar, t.type, t.function, t.clazz)
                .setUsageInfo(t.usage)
                .buffs(t.buffs);
    }
}
