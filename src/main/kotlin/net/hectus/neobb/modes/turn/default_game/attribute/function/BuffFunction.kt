package net.hectus.neobb.modes.turn.default_game.attribute.function

import net.hectus.neobb.buff.Buff

interface BuffFunction : Function {
    fun buffs(): List<Buff<*>>
}
