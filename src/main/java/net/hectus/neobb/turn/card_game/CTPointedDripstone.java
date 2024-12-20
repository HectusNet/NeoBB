package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class CTPointedDripstone extends CardTurn {
    public CTPointedDripstone(NeoPlayer player) { super(player); }
    public CTPointedDripstone(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 6; }
}