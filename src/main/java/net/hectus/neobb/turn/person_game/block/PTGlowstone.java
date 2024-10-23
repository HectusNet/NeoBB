package net.hectus.neobb.turn.person_game.block;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.UtilityCategory;
import org.bukkit.block.Block;

public class PTGlowstone extends BlockTurn implements UtilityCategory {
    public PTGlowstone(NeoPlayer player) { super(player); }
    public PTGlowstone(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public void apply() {
        super.apply();
        new Buff.ExtraTurn(Buff.BuffTarget.YOU, Randomizer.boolByChance(25) ? 2 : 1).apply(player);
    }
}
