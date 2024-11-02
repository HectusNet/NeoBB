package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class CTFlowerPot extends CardTurn {
    public CTFlowerPot(NeoPlayer player) { super(player); }
    public CTFlowerPot(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 2; }
}
