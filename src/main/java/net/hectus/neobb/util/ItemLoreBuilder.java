package net.hectus.neobb.util;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.text.Formatter;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import net.hectus.neobb.turn.default_game.attributes.function.*;
import net.hectus.neobb.turn.default_game.attributes.usage.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ItemLoreBuilder {
    private static final Component SEPARATOR = Component.text("                    ", Colors.EXTRA);

    private final Turn<?> turn;

    public ItemLoreBuilder(Turn<?> turn) {
        this.turn = turn;
    }

    public @NotNull List<Component> build(Locale l) {
        List<Component> lore = new ArrayList<>();

        lore.add(SEPARATOR);
        lore.add(key(l, "item-lore.cost.key", "₪").append(Translation.component(l, "item-lore.cost.value", turn.cost()).color(net.kyori.adventure.text.format.NamedTextColor.GOLD)));
        lore.add(SEPARATOR);
        lore.add(key(l, "info.usage.usage", "❖").append(Translation.component(l, "info.usage." + usage())));
        lore.add(key(l, "info.function.function", "❖").append(Translation.component(l, "info.function." + function())));
        lore.add(key(l, "info.class.class", "❖").append(Translation.component(l, "info.class." + clazz())));
        lore.add(SEPARATOR);
        lore.add(key(l, "item-lore.description", "❖"));
        lore.addAll(longText(l, "description"));
        if (turn.requiresUsageGuide()) {
            lore.add(SEPARATOR);
            lore.add(key(l, "item-lore.usage", "➽"));
            lore.addAll(longText(l, "usage"));
        }
        if (turn instanceof CounterFunction counter) {
            lore.add(SEPARATOR);
            lore.add(key(l, "item-lore.counters", "🛡"));
            lore.addAll(counter.counters().stream().map(c -> c.line(l)).toList());
        }
        if (turn instanceof BuffFunction buff) {
            lore.add(SEPARATOR);
            lore.add(key(l, "item-lore.buffs", "↑"));
            lore.addAll(buff.buffs().stream().map(b -> b.line(l)).toList());
        }

        return lore;
    }

    private @NotNull Component key(Locale l, String key, String icon) {
        return Component.text(icon + " ").append(Translation.component(l, key)).append(Component.text(": "))
                .color(Colors.NEUTRAL).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    private @NotNull List<? extends Component> longText(Locale l, String type) {
        String text = Translation.string(l, type + "." + turn.item().getType().name().toLowerCase());
        return Formatter.lineWrap(text, 50).stream()
                .map(s -> Component.text("   | " + s, Colors.BLUE)
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .toList();
    }

    private @NotNull String clazz() {
        if (turn instanceof ColdClazz) return "cold";
        if (turn instanceof HotClazz) return "hot";
        if (turn instanceof NatureClazz) return "nature";
        if (turn instanceof NeutralClazz) return "neutral";
        if (turn instanceof RedstoneClazz) return "redstone";
        if (turn instanceof SupernaturalClazz) return "supernatural";
        if (turn instanceof WaterClazz) return "water";
        return "other";
    }

    private @NotNull String function() {
        if (turn instanceof CounterattackFunction) return "counterattack";
        if (turn instanceof CounterbuffFunction) return "counterbuff";
        if (turn instanceof AttackFunction) return "attack";
        if (turn instanceof BuffFunction) return "buff";
        if (turn instanceof CounterFunction) return "counter";
        if (turn instanceof DefenseFunction) return "defense";
        if (turn instanceof EventFunction) return "event";
        return "other";
    }

    private @NotNull String usage() {
        if (turn instanceof BlockUsage) return "block";
        if (turn instanceof ItemUsage) return "item";
        if (turn instanceof MobUsage) return "mob";
        if (turn instanceof StructureUsage) return "structure";
        if (turn instanceof ThrowableUsage) return "throwable";
        return "other";
    }
}
