package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import org.bukkit.block.Block;

public class TComposter extends BlockTurn implements AttackFunction, NatureClazz {
    public TComposter(NeoPlayer player) { super(player); }
    public TComposter(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 2;
    }
}
