package net.hectus.neobb.turn.person_game.block;

import com.marcpg.libpg.util.MinecraftTime;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.CounterCategory;
import org.bukkit.block.Block;

public class PTDaylightDetector extends BlockTurn implements CounterCategory {
    public PTDaylightDetector(NeoPlayer player) { super(player); }
    public PTDaylightDetector(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public void apply() {
        super.apply();
        player.game.time(MinecraftTime.NOON);
    }
}
