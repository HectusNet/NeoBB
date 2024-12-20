package net.hectus.neobb.player;

import com.marcpg.libpg.storing.Cord;
import org.bukkit.Sound;

import java.util.Locale;
import java.util.UUID;

public interface Target extends net.kyori.adventure.audience.ForwardingAudience {
    String name();
    UUID uuid();
    default Locale locale() { return Locale.getDefault(); }
    void playSound(Sound sound, float volume);
    void teleport(Cord cord);
}
