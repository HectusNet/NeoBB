package net.hectus.neobb.turn.default_game.block;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TPinkBed extends BlockTurn implements BuffFunction, SupernaturalClazz {
    public TPinkBed(NeoPlayer player) { super(player); }
    public TPinkBed(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public void apply() {
        if (Randomizer.boolByChance(70)) {
            player.player.clearActivePotionEffects();
        }
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }
}
