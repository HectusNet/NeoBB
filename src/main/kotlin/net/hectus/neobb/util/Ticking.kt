package net.hectus.neobb.util

interface Ticking {
    fun tick(tick: Tick)

    data class Tick(val number: Int) {
        fun isSecond(): Boolean = number % 20 == 0
    }
}