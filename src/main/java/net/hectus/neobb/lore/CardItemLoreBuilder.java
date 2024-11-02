package net.hectus.neobb.lore;

import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CardItemLoreBuilder extends ItemLoreBuilder {
    @Override
    public List<Component> build(Locale l) {
        List<Component> lore = new ArrayList<>();

        lore.add(SEPARATOR);
        lore.add(key(l, "item-lore.damage", "❖").append(Component.text(turn.damage())));
        if (turn.requiresUsageGuide()) {
            lore.add(SEPARATOR);
            lore.add(key(l, "item-lore.usage", "➽"));
            lore.addAll(longText(l, "usage.card-game"));
        }

        return lore;
    }
}
