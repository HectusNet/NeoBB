package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class CTOakFenceGate extends InteractableCardTurn {
    public CTOakFenceGate(NeoPlayer player) { super(player); }
    public CTOakFenceGate(Block data, NeoPlayer player) { super(data, player); }

    @Override public double damage() { return 4; }
}
