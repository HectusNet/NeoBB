package net.hectus.neobb.cosmetic

import com.destroystokyo.paper.ParticleBuilder
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Particle

enum class PlaceParticle(private val particle: Particle, private val count: Int, private val offset: Double, private val extra: Double, private val data: Any?) {
    ENCHANTMENT(Particle.ENCHANT, 75, 0.2, 0.4, null),
    PORTAL(Particle.PORTAL, 75, 0.2, 0.4, null),
    PORTAL_EXTREME(Particle.PORTAL, 250, 0.2, 3.0, null),
    EFFECT(Particle.ENTITY_EFFECT, 15, 0.2, 0.0, Color.WHITE),
    STARS(Particle.WAX_OFF, 75, 0.2, 0.4, null);

    fun spawn(location: Location) {
        val builder = ParticleBuilder(particle).count(count).location(location)

        if (offset != 0.0) builder.offset(offset, offset, offset)
        if (extra != 0.0) builder.extra(extra)
        if (data != null) builder.data(data)

        builder.spawn()
    }
}