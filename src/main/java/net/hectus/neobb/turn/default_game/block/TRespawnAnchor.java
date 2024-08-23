package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.buff.ChancedBuff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TRespawnAnchor extends BlockTurn implements BuffFunction, HotClazz {
    public TRespawnAnchor(NeoPlayer player) { super(player); }
    public TRespawnAnchor(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public boolean requiresUsageGuide() { return true; }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Teleport(Buff.BuffTarget.YOU), new ChancedBuff(new Buff.ExtraTurn(), 50));
    }
}
