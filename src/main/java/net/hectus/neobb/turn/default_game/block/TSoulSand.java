package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import org.bukkit.block.Block;

public class TSoulSand extends BlockTurn implements AttackFunction, SupernaturalClazz {
    public TSoulSand(NeoPlayer player) { super(player); }
    public TSoulSand(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 3;
    }
}
