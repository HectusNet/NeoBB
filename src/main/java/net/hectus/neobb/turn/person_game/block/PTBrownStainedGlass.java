package net.hectus.neobb.turn.person_game.block;

import com.marcpg.libpg.util.MinecraftTime;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.DefensiveCategory;
import net.hectus.neobb.turn.person_game.warp.PTVillagerWarp;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.block.Block;

public class PTBrownStainedGlass extends BlockTurn implements DefensiveCategory {
    public PTBrownStainedGlass(NeoPlayer player) { super(player); }
    public PTBrownStainedGlass(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public void apply() {
        super.apply();
        player.heal(player.game.warp() instanceof PTVillagerWarp ? 2 : 5);
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
