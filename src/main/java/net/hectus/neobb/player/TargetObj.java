package net.hectus.neobb.player;

import net.kyori.adventure.audience.Audience;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TargetObj implements Target {
    private final List<NeoPlayer> players = new ArrayList<>();

    public TargetObj(Collection<NeoPlayer> players) {
        this.players.addAll(players);
    }

    public List<NeoPlayer> players() {
        return players;
    }

    public void addPlayer(NeoPlayer player) {
        players.add(player);
    }

    @Override
    public @NotNull Iterable<? extends Audience> audiences() {
        return players;
    }
}
