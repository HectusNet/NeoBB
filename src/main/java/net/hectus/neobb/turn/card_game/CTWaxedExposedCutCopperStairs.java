package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public class CTWaxedExposedCutCopperStairs extends CardTurn {
    public CTWaxedExposedCutCopperStairs(NeoPlayer player) { super(player); }
    public CTWaxedExposedCutCopperStairs(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() { return 5; }
}
