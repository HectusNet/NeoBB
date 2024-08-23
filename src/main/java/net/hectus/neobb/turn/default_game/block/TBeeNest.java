package net.hectus.neobb.turn.default_game.block;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.mob.TBee;
import org.bukkit.block.Block;
import org.bukkit.entity.Bee;

public class TBeeNest extends BlockTurn implements AttackFunction, NatureClazz {
    public TBeeNest(NeoPlayer player) { super(player); }
    public TBeeNest(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public void apply() {
        if (Randomizer.boolByChance(20)) {
            TBee bee = new TBee(player.game.world().spawn(location(), Bee.class), player);
            bee.buffs().forEach(b -> b.apply(player));
            bee.apply();
        }
    }
}
