package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class CTTorch extends CardTurn {
    public CTTorch(NeoPlayer player) { super(player); }
    public CTTorch(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 6; }
}
