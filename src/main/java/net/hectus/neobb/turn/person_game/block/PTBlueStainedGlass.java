package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.DefensiveCategory;
import net.hectus.neobb.turn.person_game.warp.PTIceWarp;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.block.Block;

public class PTBlueStainedGlass extends BlockTurn implements DefensiveCategory {
    public PTBlueStainedGlass(NeoPlayer player) { super(player); }
    public PTBlueStainedGlass(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public void apply() {
        super.apply();
        player.heal(player.game.warp() instanceof PTIceWarp ? 2 : 5);
    }

    @Override
    public void applyDefense() {
        player.addModifier(Modifiers.P_DEFAULT_DEFENDED);
    }

    @Override
    public boolean unusable() {
        return !player.game.world().isDayTime();
    }
}
