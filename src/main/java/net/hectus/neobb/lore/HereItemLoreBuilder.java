package net.hectus.neobb.lore;

import net.hectus.neobb.turn.here_game.HereTurn;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HereItemLoreBuilder extends ItemLoreBuilder {
    @Override
    public List<Component> build(Locale l) {
        if (!(turn instanceof HereTurn hereTurn)) return List.of();

        List<Component> lore = new ArrayList<>();

        lore.add(SEPARATOR);
        lore.add(key(l, "item-lore.damage", "❖").append(Component.text(hereTurn.damage())));
        if (turn.requiresUsageGuide()) {
            lore.add(SEPARATOR);
            lore.add(key(l, "item-lore.usage", "➽"));
            lore.addAll(longText(l, "usage.here-game"));
        }

        return lore;
    }
}
