package net.hectus.neobb.player

import com.marcpg.libpg.data.modifiable.Modifiable
import com.marcpg.libpg.storing.Cord
import net.hectus.neobb.util.asCord
import net.kyori.adventure.audience.ForwardingAudience
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*

interface Target : ForwardingAudience, Modifiable {
    fun name(): String
    fun uuid(): UUID
    fun locale(): Locale = Locale.getDefault()
    override fun toString(): String

    fun playSound(sound: Sound, volume: Float = 1.0f)
    fun teleport(cord: Cord, yaw: Float = 0.0f, pitch: Float = 0.0f)
    fun teleport(location: Location)
    fun closeInv()

    fun location(): Location
    fun cord(): Cord = location().asCord()

    fun eachBukkitPlayer(action: (Player) -> Unit)
    fun eachNeoPlayer(action: (NeoPlayer) -> Unit)
}