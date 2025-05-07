package net.hectus.neobb.util

import net.hectus.neobb.util.Colors.BLUE
import net.hectus.neobb.util.Colors.GREEN
import net.hectus.neobb.util.Colors.RED
import net.hectus.neobb.util.Colors.YELLOW
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor

/**
 * Colors to be used instead of [NamedTextColor] to ensure a uniform style.
 * Some of these use colors from the original game and some are completely custom.
 */
object Colors {
    /** Primary Accent-Color (#B47ACE) */
    val ACCENT: TextColor = TextColor.color(0xB47ACE)

    /** Secondary Accent-Color (#CE2440) */
    val SECONDARY: TextColor = TextColor.color(0xCE2440)


    /** Pastel Green (#5CE673) */
    val GREEN: TextColor = TextColor.color(0x5CE673)

    /** Pastel Green (#DAE65C) */
    val YELLOW: TextColor = TextColor.color(0xDAE65C)

    /** Pastel Red (#E65C67) */
    val RED: TextColor = TextColor.color(0xE65C67)

    /** Pastel Blue (#6CAAD9) */
    val BLUE: TextColor = TextColor.color(0x6CAAD9)


    /** [Vanilla Gray][NamedTextColor]. Picked from screenshots of the original game. */
    val PERSON_0: TextColor = NamedTextColor.GRAY

    /** Dark and desaturated green (#328825). Picked from screenshots of the original game. */
    val PERSON_1: TextColor = TextColor.color(0x328825)

    /** Slightly darkened green (#37BF1F). Picked from screenshots of the original game. */
    val PERSON_2: TextColor = TextColor.color(0x37BF1F)

    /** Light and slightly pastel green (#6AF95D). Picked from screenshots of the original game. */
    val PERSON_3: TextColor = TextColor.color(0x6AF95D)

    /** Slightly desaturated cyan (#44D0B9). Picked from screenshots of the original game. */
    val PERSON_4: TextColor = TextColor.color(0x44D0B9)


    /** Positive color to be used for confirmation messages. See [GREEN]. */
    val POSITIVE: TextColor = GREEN

    /** Neutral color to be used for all types of messages. See [YELLOW]. */
    val NEUTRAL: TextColor = YELLOW

    /** Negative color to be used for failure messages. See [RED]. */
    val NEGATIVE: TextColor = RED

    /** Color for links. See [BLUE]. */
    val LINK: TextColor = BLUE


    /** Dark gray color for unimportant text. See [NamedTextColor.DARK_GRAY]. */
    val EXTRA: TextColor = NamedTextColor.DARK_GRAY

    /** Utility color for resetting text back to white. See [NamedTextColor.WHITE]. */
    val RESET: TextColor = NamedTextColor.WHITE
}
