package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class CTChest extends CardTurn {
    public CTChest(NeoPlayer player) { super(player); }
    public CTChest(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 5; }
}
