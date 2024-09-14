package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.block.Block;

public class TBlueOrchid extends FlowerTurn {
    public TBlueOrchid(NeoPlayer player) { super(player); }
    public TBlueOrchid(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public void apply() {
        player.addModifier(Modifiers.P_DEFAULT_100P_WARP);
    }
}
