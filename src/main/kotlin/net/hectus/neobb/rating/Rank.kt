package net.hectus.neobb.rating

import net.hectus.neobb.util.component
import net.hectus.neobb.util.makeCapitalized
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material
import java.util.*

enum class Rank(val minElo: Double, val levels: Int, val league: Int, val material: Material, val color: TextColor) {
    DIRT(100.0, 1, 1, Material.DIRT, TextColor.color(0x907050)),
    WOOD(250.0, 1, 1, Material.OAK_PLANKS, TextColor.color(0xBCA988)),
    STONE(500.0, 1, 1, Material.STONE, TextColor.color(0x88888A)),
    DEEPSLATE(1000.0, 1, 1, Material.DEEPSLATE, TextColor.color(0x565655)),
    NETHERRACK(1250.0, 1, 1, Material.NETHERRACK, TextColor.color(0x743435)),
    COAL(1500.0, 2, 2, Material.COAL, TextColor.color(0x3f3f3f)),
    COPPER(1600.0, 2, 2, Material.COPPER_INGOT, TextColor.color(0xC86E53)),
    IRON(1700.0, 2, 2, Material.IRON_INGOT, TextColor.color(0xD8D8D8)),
    DIAMOND(1800.0, 2, 2, Material.DIAMOND, TextColor.color(0x8BF4E3)),
    NETHERITE(1900.0, 2, 2, Material.NETHERITE_INGOT, TextColor.color(0x573E37)),
    PURPUR(2000.0, 3, 3, Material.PURPUR_BLOCK, TextColor.color(0xB383B3)),
    AMETHYST(2150.0, 3, 3, Material.AMETHYST_SHARD, TextColor.color(0xCF96FF)),
    SCULK(2500.0, 3, 3, Material.ECHO_SHARD, TextColor.color(0x1CA7A2)),
    ENDER(2750.0, 3, 3, Material.ENDER_EYE, TextColor.color(0xB6E15F)),
    CHAMP(3000.0, 5, 4, Material.DRAGON_EGG, TextColor.color(0xF2D114)),
    BEDROCK(5000.0, 1, 5, Material.BEDROCK, TextColor.color(0x777777));

    val maxElo: Double
        get() = entries.getOrNull(ordinal + 1)?.minElo ?: Double.MAX_VALUE

    companion object {
        fun ofElo(elo: Double): Pair<Rank, Int> {
            for (rank in entries) {
                if (rank == BEDROCK) continue

                if (elo >= rank.minElo && elo < rank.maxElo) {
                    val levelStep = (rank.maxElo - rank.minElo) / rank.levels

                    var current = rank.maxElo
                    var level = rank.levels
                    while (current > rank.minElo) {
                        current -= levelStep
                        level--
                        if (elo >= current) {
                            return rank to level
                        }
                    }
                    return rank to 1
                }
            }
            return Rank.DIRT to 1
        }

        fun Pair<Rank, Int>.toRankString() = first.name.makeCapitalized() + " " + second

        fun Pair<Rank, Int>.toRankTranslations(locale: Locale) = locale
            .component("rating.rank.${first.name.lowercase()}")
            .append(Component.text(second.toRomanNumeral()))
            .color(first.color)

        private fun Int.toRomanNumeral(): String {
            return when (this) {
                1 -> "I"
                2 -> "II"
                3 -> "III"
                4 -> "IV"
                5 -> "V"
                else -> "V+"
            }
        }
    }
}
