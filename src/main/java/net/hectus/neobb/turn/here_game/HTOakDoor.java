package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class HTOakDoor extends InteractableHereTurn { // TODO: Register Interaction
    public HTOakDoor(NeoPlayer player) { super(player); }
    public HTOakDoor(Block data, NeoPlayer player) { super(data, player); }

    @Override public double damage() { return 4; }
    @Override public int maxInteractions() { return 3; }
    @Override public long interactionIntervalMs() { return 1000; }
}
