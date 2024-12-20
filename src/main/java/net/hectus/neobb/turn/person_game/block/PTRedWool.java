package net.hectus.neobb.turn.person_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.turn.person_game.categorization.AttackCategory;
import net.hectus.neobb.turn.person_game.warp.PTFireWarp;
import org.bukkit.block.Block;

import java.util.List;

public class PTRedWool extends BlockTurn implements AttackCategory {
    public PTRedWool(NeoPlayer player) { super(player); }
    public PTRedWool(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() {
        return player.game.warp() instanceof PTFireWarp ? 2 : 4;
    }

    @Override
    public List<Class<? extends Turn<?>>> counteredBy() {
        return List.of(PTBarrel.class, PTPinkCarpet.class);
    }
}