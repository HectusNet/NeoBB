package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class HTOakFenceGate extends InteractableHereTurn {
    public HTOakFenceGate(NeoPlayer player) { super(player); }
    public HTOakFenceGate(Block data, NeoPlayer player) { super(data, player); }

    @Override public double damage() { return 4; }
    @Override public int maxInteractions() { return 3; }
    @Override public long interactionIntervalMs() { return 1000; }
}
