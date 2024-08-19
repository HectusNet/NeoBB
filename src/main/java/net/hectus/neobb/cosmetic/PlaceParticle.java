package net.hectus.neobb.cosmetic;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.awt.*;

public enum PlaceParticle {
    ENCHANTMENT(Particle.ENCHANT, 75, 0.2, 0.4, null),
    PORTAL(Particle.PORTAL, 75, 0.2, 0.4, null),
    PORTAL_EXTREME(Particle.PORTAL, 250, 0.2, 3.0, null),
    EFFECT(Particle.ENTITY_EFFECT, 15, 0.2, 0, Color.WHITE),
    STARS(Particle.WAX_OFF, 75, 0.2, 0.4, null);

    public final Particle particle;
    public final int count;
    public final double offset;
    public final double extra;
    public final Object data;

    PlaceParticle(Particle particle, int count, double offset, double extra, Object data) {
        this.particle = particle;
        this.count = count;
        this.offset = offset;
        this.extra = extra;
        this.data = data;
    }

    public void spawn(Location location) {
        ParticleBuilder builder = new ParticleBuilder(particle).count(count).location(location);

        if (offset != 0) builder.offset(offset, offset, offset);
        if (extra != 0) builder.extra(extra);
        if (data != null) builder.data(data);

        builder.spawn();
    }
}
