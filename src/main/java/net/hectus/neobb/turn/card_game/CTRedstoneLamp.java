package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class CTRedstoneLamp extends CardTurn {
    public CTRedstoneLamp(NeoPlayer player) { super(player); }
    public CTRedstoneLamp(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 5; }
}
