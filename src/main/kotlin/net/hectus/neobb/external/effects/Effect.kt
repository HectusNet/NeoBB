package net.hectus.neobb.external.effects

import org.bukkit.Location

abstract class Effect(
    archPoint: Location,
    anchorPoint: Location
) {
    abstract fun play()
}
