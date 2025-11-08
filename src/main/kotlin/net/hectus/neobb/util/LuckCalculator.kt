package net.hectus.neobb.util

import kotlin.math.E
import kotlin.math.pow
import kotlin.random.Random

object LuckCalculator {
    const val A = 0.02
    const val C = 0.007

    fun exponential(a: Double, l: Int) = E.pow(a * (l - Constants.BASE_LUCK).toDouble())

    fun decayK(l: Int): Double = exponential(-A, l)
    fun decayT(l: Int): Double = exponential(-C, l)

    fun growthK(l: Int): Double = exponential(A, l)
    fun growthT(l: Int): Double = exponential(C, l)

    fun luck(p: Double, l: Int): Double = (p / decayT(l)).pow(decayK(l)).coerceIn(0.0, 1.0)
    fun loss(p: Double, l: Int): Double = (p / growthT(l)).pow(growthK(l)).coerceIn(0.0, 1.0)
}

fun Double.luck(l: Int): Double = LuckCalculator.luck(this, l)
fun Double.luckChance(l: Int): Boolean = Random.nextDouble() < luck(l)

fun Double.loss(l: Int): Double = LuckCalculator.loss(this, l)
fun Double.lossChance(l: Int): Boolean = Random.nextDouble() < loss(l)
