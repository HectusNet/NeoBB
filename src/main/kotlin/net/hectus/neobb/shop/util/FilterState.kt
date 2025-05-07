package net.hectus.neobb.shop.util

import net.hectus.neobb.util.Colors
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material

enum class FilterState(val item: Material, val color: TextColor) {
    YES(Material.LIME_DYE, Colors.POSITIVE),
    NO(Material.RED_DYE, Colors.NEGATIVE),
    UNSET(Material.WHITE_DYE, Colors.EXTRA)
}
