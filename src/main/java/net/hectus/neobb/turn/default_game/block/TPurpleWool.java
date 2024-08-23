package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import org.bukkit.block.Block;

import java.util.List;

public class TPurpleWool extends BlockTurn implements AttackFunction, NeutralClazz {
    public TPurpleWool(NeoPlayer player) { super(player); }
    public TPurpleWool(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public boolean canBeUsed() {
        List<Turn<?>> history = player.game.history();
        if (history.size() < 5) return false;

        return history.subList(history.size() - 5, history.size()).stream()
                .noneMatch(turn -> turn instanceof AttackFunction);
    }

    @Override
    public void apply() {
        player.game.win(player);
    }

    @Override
    public int cost() {
        return 4;
    }
}
