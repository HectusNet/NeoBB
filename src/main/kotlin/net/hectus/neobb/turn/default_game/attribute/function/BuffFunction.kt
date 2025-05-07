package net.hectus.neobb.turn.default_game.attribute.function

import net.hectus.neobb.buff.Buff

interface BuffFunction : Function {
    fun buffs(): List<Buff<*>>
}
