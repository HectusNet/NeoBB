package net.hectus.neobb.turn.legacy_game.block;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.block.BlockTurn;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.block.Block;

import java.util.List;

public class LTTnt extends BlockTurn implements AttackFunction, RedstoneClazz {
    public LTTnt(NeoPlayer player) { super(player); }
    public LTTnt(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 2;
    }

    @Override
    public void apply() {
        List<NeoPlayer> eliminations = player.game.players().stream().filter(p -> !p.hasModifier(Modifiers.P_DEFAULT_DEFENDED)).toList();
        if (eliminations.size() >= player.game.players().size()) {
            player.game.draw(false);
        } else {
            eliminations.forEach(player.game::eliminatePlayer);
        }
    }
}
