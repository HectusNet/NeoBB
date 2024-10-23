package net.hectus.neobb.util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import static net.kyori.adventure.text.format.TextColor.color;

public final class Colors {
    public static final TextColor ACCENT = color(0xB47ACE);
    public static final TextColor SECONDARY = color(0xCE2440);
    public static final TextColor GREEN = color(0x5CE673);
    public static final TextColor YELLOW = color(0xDAE65C);
    public static final TextColor RED = color(0xE65C67);
    public static final TextColor BLUE = color(0x6CAAD9);

    // Got these from screenshots of the original game.
    public static final TextColor PERSON_0 = NamedTextColor.GRAY;
    public static final TextColor PERSON_1 = color(0x328825);
    public static final TextColor PERSON_2 = color(0x37BF1F);
    public static final TextColor PERSON_3 = color(0x6AF95D);
    public static final TextColor PERSON_4 = color(0x44D0B9);

    public static final TextColor POSITIVE = GREEN;
    public static final TextColor NEUTRAL = YELLOW;
    public static final TextColor NEGATIVE = RED;
    public static final TextColor LINK = BLUE;

    public static final TextColor EXTRA = NamedTextColor.DARK_GRAY;
    public static final TextColor RESET = NamedTextColor.WHITE;
}
