package net.hectus.neobb.shop.util;

import net.hectus.neobb.util.Colors;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public enum FilterState {
    YES(Material.LIME_STAINED_GLASS_PANE, Colors.POSITIVE),
    NO(Material.RED_STAINED_GLASS_PANE, Colors.NEGATIVE),
    UNSET(Material.GRAY_STAINED_GLASS_PANE, Colors.EXTRA);

    public final Material item;
    public final TextColor color;

    FilterState(Material item, TextColor color) {
        this.item = item;
        this.color = color;
    }
}
