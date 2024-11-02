package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class CTDaylightDetector extends CardTurn {
    public CTDaylightDetector(NeoPlayer player) { super(player); }
    public CTDaylightDetector(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 3; }
}
