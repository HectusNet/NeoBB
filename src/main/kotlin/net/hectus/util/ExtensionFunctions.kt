package net.hectus.util

import com.marcpg.libpg.lang.Translation
import com.marcpg.libpg.storing.Cord
import com.marcpg.libpg.storing.CordMinecraftAdapter
import net.hectus.neobb.modes.turn.Turn
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.World
import java.text.MessageFormat
import java.util.*
import kotlin.reflect.KClass

fun <T> List<T>.following(element: T): T? {
    val index = indexOf(element)
    return if (index != -1) this[(index + 1) % size] else null
}

fun Component.asString(): String = PlainTextComponentSerializer.plainText().serialize(this)
fun Location.asCord(): Cord = CordMinecraftAdapter.ofLocation(this)
fun Cord.asLocation(world: World): Location = CordMinecraftAdapter.toLocation(this, world)
fun Locale.component(key: String, vararg variables: String?, color: TextColor? = null, decoration: TextDecoration? = null): Component {
    var component = Component.text(string(key, *variables))
    if (color != null)
        component = component.color(color)
    if (decoration != null)
        component = component.decorate(decoration)
    return component
}

fun Locale.string(key: String, vararg variables: String?): String {
    val plainString = Translation.string(this, key)
    return if (variables.isEmpty()) plainString else MessageFormat.format(plainString, *variables)
}

fun KClass<out Turn<*>>.material(): Material = enumValueNoCase(simpleName!!.counterFilterName().camelToSnake())
