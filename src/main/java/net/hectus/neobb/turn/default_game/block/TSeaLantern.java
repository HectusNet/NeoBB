package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TSeaLantern extends BlockTurn implements BuffFunction, WaterClazz {
    public TSeaLantern(NeoPlayer player) { super(player); }
    public TSeaLantern(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 7;
    }

    @Override
    public void apply() {
        // TODO: Give revive for next 3 turns.
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }
}
