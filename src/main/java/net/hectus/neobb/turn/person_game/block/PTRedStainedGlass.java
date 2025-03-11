package net.hectus.neobb.turn.person_game.block;

import com.marcpg.libpg.util.MinecraftTime;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.DefensiveCategory;
import net.hectus.neobb.turn.person_game.warp.PTFireWarp;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.block.Block;

public class PTRedStainedGlass extends BlockTurn implements DefensiveCategory {
    public PTRedStainedGlass(NeoPlayer player) { super(player); }
    public PTRedStainedGlass(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public void apply() {
        super.apply();
        player.heal(player.game.warp() instanceof PTFireWarp ? 2 : 5);
    }

    @Override
    public void applyDefense() {
        player.addModifier(Modifiers.P_DEFAULT_DEFENDED);
    }

    @Override
    public boolean unusable() {
        return player.game.time() == MinecraftTime.MIDNIGHT;
    }
}
