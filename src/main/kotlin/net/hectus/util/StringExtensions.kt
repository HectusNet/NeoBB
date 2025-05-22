package net.hectus.util

private val NUMBER_REGEX = Regex("\\d+")
fun String.extractNumber(): Int = NUMBER_REGEX.find(this)?.value?.toInt() ?: 0

private val COUNTER_FILTER_REGEX = Regex("^.?T(?!.*(Function|Usage|Clazz)$)|Function$|Usage$|Clazz$")
private val CAMEL_CASE_REGEX = Regex("([a-z])([A-Z]+)")

fun String.makeCapitalized(): String = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
fun String.camelToSnake(): String = replace(CAMEL_CASE_REGEX, "$1_$2").lowercase()
fun String.camelToTitle(): String = replace(CAMEL_CASE_REGEX, "$1 $2")
fun String.counterFilterName(): String = replaceFirst(COUNTER_FILTER_REGEX, "")
