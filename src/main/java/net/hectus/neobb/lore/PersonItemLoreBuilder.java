package net.hectus.neobb.lore;

import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.turn.person_game.categorization.Category;
import net.hectus.neobb.turn.person_game.warp.PWarpTurn;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PersonItemLoreBuilder extends ItemLoreBuilder {
    @Override
    public List<Component> build(Locale l) {
        if (!(turn instanceof Category category)) return List.of();

        List<Component> lore = new ArrayList<>();

        if (turn.damage() > 0.0) {
            lore.add(Component.text("Damage: ", category.categoryColor(), TextDecoration.BOLD)
                    .append(Component.text(turn.damage() + "hearts.")));
        }

        if (turn instanceof CounterFunction counter) {
            lore.add(Translation.component(l, "item-lore.counters").color(category.categoryColor()).decorate(TextDecoration.BOLD)
                    .append(Component.text(String.join(", ", counter.counters().stream().map(c -> c.text(l)).toList()))));
        }
        if (turn instanceof PWarpTurn warp) {
            lore.add(Component.text("Probability: ", category.categoryColor(), TextDecoration.BOLD)
                    .append(Component.text(warp.chance() + "%")));
        }
        if (turn instanceof BuffFunction buff) {
            lore.addAll(buff.buffs().stream().map(b -> Component.text("Probability: ", category.categoryColor(), TextDecoration.BOLD)
                    .append(Component.text(b.text(l) + b.target(l), b.color()))).toList());
        }

        return lore;
    }
}
