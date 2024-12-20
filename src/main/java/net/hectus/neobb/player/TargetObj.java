package net.hectus.neobb.player;

import com.marcpg.libpg.storing.Cord;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TargetObj implements Target {
    private final List<NeoPlayer> players = new ArrayList<>();

    public TargetObj(Collection<NeoPlayer> players) {
        this.players.addAll(players);
    }

    public List<NeoPlayer> players() {
        return players;
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return players;
    }

    @Override
    public String name() {
        return players.stream().map(NeoPlayer::name).collect(Collectors.joining(", "));
    }

    @Override
    public UUID uuid() {
        long mostSigBits = 0L, leastSigBits = 0L;
        for (NeoPlayer p : players) {
            mostSigBits += p.uuid().getMostSignificantBits();
            leastSigBits += p.uuid().getLeastSignificantBits();
        }
        return new UUID(mostSigBits, leastSigBits);
    }

    @Override
    public void playSound(Sound sound, float volume) {
        players.forEach(p -> p.playSound(sound, volume));
    }

    @Override
    public void teleport(Cord cord) {
        players.forEach(p -> p.teleport(cord));
    }
}
