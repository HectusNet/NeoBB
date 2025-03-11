package net.hectus.neobb.util;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;

import static net.kyori.adventure.text.format.TextColor.color;

/**
 * Colors to be used instead of {@link NamedTextColor} to ensure a uniform style.
 * Some of these use colors from the original game and some are completely custom.
 */
public final class Colors {
    /** Primary accent color. */ public static final TextColor ACCENT = color(0xB47ACE);
    /** Secondary accent color. */ public static final TextColor SECONDARY = color(0xCE2440);
    /** Pastel Green. */ public static final TextColor GREEN = color(0x5CE673);
    /** Pastel Yellow. */ public static final TextColor YELLOW = color(0xDAE65C);
    /** Pastel Red. */ public static final TextColor RED = color(0xE65C67);
    /** Pastel Blue. */ public static final TextColor BLUE = color(0x6CAAD9);

    // Got these from screenshots of the original game.
    /** {@link NamedTextColor#GRAY Vanilla Gray} (Person Mode). */ public static final TextColor PERSON_0 = NamedTextColor.GRAY;
    /** Dark Green (Person Mode). */ public static final TextColor PERSON_1 = color(0x328825);
    /** Green (Person Mode). */ public static final TextColor PERSON_2 = color(0x37BF1F);
    /** Light Green (Person Mode). */ public static final TextColor PERSON_3 = color(0x6AF95D);
    /** Desaturated Cyan (Person Mode). */ public static final TextColor PERSON_4 = color(0x44D0B9);

    /** Pastel Green, see {@link #GREEN}. */ public static final TextColor POSITIVE = GREEN;
    /** Pastel Yellow, see {@link #YELLOW}. */ public static final TextColor NEUTRAL = YELLOW;
    /** Pastel Red, see {@link #RED}. */ public static final TextColor NEGATIVE = RED;
    /** Pastel Blue, see {@link #BLUE}. */ public static final TextColor LINK = BLUE;

    /** {@link NamedTextColor#DARK_GRAY Vanilla Dark Gray}. */ public static final TextColor EXTRA = NamedTextColor.DARK_GRAY;
    /** {@link NamedTextColor#WHITE Vanilla White}. */ public static final TextColor RESET = NamedTextColor.WHITE;
}
