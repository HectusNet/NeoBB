package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class HTOakDoor extends InteractableHereTurn {
    public HTOakDoor(NeoPlayer player) { super(player); }
    public HTOakDoor(Block data, NeoPlayer player) { super(data, player); }

    @Override public double damage() { return 4; }
}
