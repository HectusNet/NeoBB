package net.hectus.neobb.lore;

import com.marcpg.libpg.lang.Translation;
import com.marcpg.libpg.text.Formatter;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Utilities;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

public abstract class ItemLoreBuilder {
    public static final Component SEPARATOR = Component.text("                    ", Colors.EXTRA);

    protected Turn<?> turn = null;

    public abstract List<Component> build(Locale l);

    public ItemLoreBuilder turn(Turn<?> turn) {
        this.turn = turn;
        return this;
    }

    protected @NotNull Component key(Locale l, String key, String icon) {
        return Component.text(icon + " ").append(Translation.component(l, key)).append(Component.text(": "))
                .color(Colors.NEUTRAL).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    protected @NotNull List<? extends Component> longText(Locale l, String type) {
        String text = Translation.string(l, type + "." + Utilities.camelToSnake(Utilities.counterFilterName(turn.getClass().getSimpleName())));
        return Formatter.lineWrap(text, 50).stream()
                .map(s -> Component.text("   | " + s, Colors.BLUE)
                        .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE))
                .toList();
    }
}
