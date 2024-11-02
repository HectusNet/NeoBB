package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class CTOakDoor extends InteractableCardTurn {
    public CTOakDoor(NeoPlayer player) { super(player); }
    public CTOakDoor(Block data, NeoPlayer player) { super(data, player); }

    @Override public double damage() { return 4; }
}
