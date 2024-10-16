package net.hectus.neobb.lore;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.storing.Pair;
import net.hectus.neobb.turn.DummyTurn;
import net.hectus.neobb.turn.default_game.attributes.function.*;
import net.hectus.neobb.util.Colors;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LegacyItemLoreBuilder extends ItemLoreBuilder {
    public @NotNull List<Component> build(Locale l) {
        List<Component> lore = new ArrayList<>();

        lore.add(key(l, "info.type.type", "❖").color(Colors.YELLOW).append(Translation.component(l, "info.type." + type()).color(Colors.RESET)));
        lore.add(key(l, "info.hotbar.hotbar", "❖").color(Colors.YELLOW).append(Translation.component(l, "info.hotbar.both").color(Colors.RESET))); // TODO: Implement dual-hotbar system.

        Pair<String, String> rarity = rarity();
        lore.add(key(l, "item-lore.rarity", "❖").color(Colors.YELLOW).append(Component.text(rarity.right(), Colors.RESET).appendSpace()).append(Translation.component(l, "info.rarity." + rarity().left()).color(Colors.RESET)));

        lore.add(key(l, "item-lore.description", "❖"));
        lore.addAll(longText(l, "description"));

        if (turn instanceof CounterFunction counter) {
            lore.add(key(l, "item-lore.counters", "⚔"));
            lore.addAll(counter.counters().stream().map(c -> c.line(l)).toList());
        }

        if (turn.requiresUsageGuide()) {
            lore.add(SEPARATOR);
            lore.add(key(l, "item-lore.usage", "➽"));
            lore.addAll(longText(l, "usage"));
        }

        if (turn instanceof BuffFunction buff) {
            lore.add(SEPARATOR);
            lore.add(key(l, "item-lore.buffs", "↑"));
            lore.addAll(buff.buffs().stream().map(b -> b.line(l)).toList());
        }

        lore.add(SEPARATOR);
        Pair<String, Character> quality = quality();
        lore.add(key(l, "item.quality.quality", "✪").color(Colors.YELLOW).append(Component.text(quality.right(), Colors.RESET).appendSpace()).append(Translation.component(l, "info.quality." + quality().left()).color(Colors.RESET)));
        lore.add(key(l, "item-lore.cost.key", "₪").color(Colors.YELLOW).append(Translation.component(l, "item-lore.cost.value", turn.cost()).color(Colors.RESET)));

        return lore;
    }

    private @NotNull String type() {
        if (turn instanceof WarpFunction) return "warp";
        if (turn instanceof DummyTurn) return "await";
        if (turn instanceof CounterattackFunction) return "counterattack";
        if (turn instanceof AttackFunction) return "attack";
        if (turn instanceof CounterFunction) return "counter";
        if (turn instanceof BuffFunction) return "buff";
        if (turn instanceof DefenseFunction) return "defense";
        return "other";
    }

    private @NotNull Pair<String, String> rarity() {
        return Pair.of("common", "✫"); // TODO: Use some cool logic to determine the item rarity.
    }

    private @NotNull Pair<String, Character> quality() {
        if (turn.cost() >= 7) return Pair.of("perfect", '➄');
        if (turn.cost() >= 5) return Pair.of("good", '➃');
        if (turn.cost() >= 4) return Pair.of("decent", '➂');
        if (turn.cost() >= 3) return Pair.of("bad", '➁');
        return Pair.of("poor", '➀');
    }
}
