package net.hectus.neobb.util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import static net.kyori.adventure.text.format.TextColor.color;

public final class Colors {
    public static final TextColor ACCENT = color(0xB47ACE);
    public static final TextColor GREEN = color(0x5CE673);
    public static final TextColor YELLOW = color(0xDAE65C);
    public static final TextColor RED = color(0xE65C67);
    public static final TextColor BLUE = color(0x6CAAD9);

    public static final TextColor POSITIVE = GREEN;
    public static final TextColor NEUTRAL = YELLOW;
    public static final TextColor NEGATIVE = RED;
    public static final TextColor LINK = BLUE;

    public static final TextColor EXTRA = NamedTextColor.DARK_GRAY;
    public static final TextColor RESET = NamedTextColor.WHITE;
}
