package net.hectus.neobb.turn.person_game.categorization

import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.player.NeoPlayer
import net.hectus.neobb.turn.Turn
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Material

interface DefensiveCounterCategory : CounterCategory {
    override val categoryColor: TextColor get() = NamedTextColor.DARK_GREEN

    override val categoryItem: Material get() = Material.LEVER
    override val categoryName: String get() = "defensive_counter"

    override fun properlyPlaced(counter: Cord, attack: Cord): Boolean {
        return attack.distance(counter) <= 2.25 // Small buffer, just in case.
    }

    override fun counter(source: NeoPlayer, countered: Turn<*>) {
        super.counter(source, countered)
        countered.player!!.damage(2.0)
    }
}
