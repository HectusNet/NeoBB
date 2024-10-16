package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class FlowerTurn extends BlockTurn implements BuffFunction, NatureClazz {
    public FlowerTurn(NeoPlayer player) { super(player); }
    public FlowerTurn(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public boolean goodChoice(@NotNull NeoPlayer player) {
        if (player.game.history().isEmpty()) return false;
        Turn<?> last = player.game.history().getLast();
        return super.goodChoice(player) && (last instanceof TDirt || last instanceof TFlowerPot);
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }
}
