package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class CTOakTrapdoor extends CardTurn {
    public CTOakTrapdoor(NeoPlayer player) { super(player); }
    public CTOakTrapdoor(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 8; }
}