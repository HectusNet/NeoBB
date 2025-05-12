package net.hectus.neobb.player

import com.marcpg.libpg.data.modifiable.ModifiableImpl
import com.marcpg.libpg.storing.Cord
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*
import java.util.stream.Collectors

open class ForwardingTarget(private val players: MutableList<NeoPlayer>): ModifiableImpl(), Target {
    override fun audiences(): MutableList<NeoPlayer> = players

    override fun addModifier(modifier: Any?) {
        players.forEach { it.addModifier(modifier) }
    }

    override fun removeModifier(modifier: Any?) {
        players.forEach { it.removeModifier(modifier) }
    }

    override fun hasModifier(modifier: Any?): Boolean {
        return players.any { it.hasModifier(modifier) }
    }

    override fun name(): String {
        return players.stream().map { it.name() }.collect(Collectors.joining(", "))
    }

    override fun uuid(): UUID {
        var mostSigBits = 0L
        var leastSigBits = 0L
        for (p in players) {
            mostSigBits += p.uuid().mostSignificantBits
            leastSigBits += p.uuid().leastSignificantBits
        }
        return UUID(mostSigBits, leastSigBits)
    }

    override fun toString(): String = name()

    override fun playSound(sound: Sound, volume: Float) {
        players.forEach { it.playSound(sound, volume) }
    }

    override fun teleport(cord: Cord, yaw: Float, pitch: Float) {
        players.forEach { it.teleport(cord, yaw, pitch) }
    }

    override fun teleport(location: Location) {
        players.forEach { it.teleport(location) }
    }

    override fun closeInv() {
        players.forEach { it.closeInv() }
    }

    override fun location(): Location {
        if (players.isEmpty()) return Location(null, 0.0, 0.0, 0.0)

        val locations = players.stream().map { it.location() }
        return Location(
            players.first().location().world,
            locations.mapToDouble { it.x }.average().orElseThrow(),
            locations.mapToDouble { it.y }.average().orElseThrow(),
            locations.mapToDouble { it.z }.average().orElseThrow()
        )
    }

    override fun eachBukkitPlayer(action: (Player) -> Unit) {
        players.forEach { it.eachBukkitPlayer(action) }
    }

    override fun eachNeoPlayer(action: (NeoPlayer) -> Unit) {
        players.forEach { it.eachNeoPlayer(action) }
    }

    override fun sendMessage(key: String, vararg variables: String?, color: TextColor?, decoration: TextDecoration?) {
        players.forEach { it.sendMessage(key, *variables, color = color, decoration = decoration) }
    }
}
