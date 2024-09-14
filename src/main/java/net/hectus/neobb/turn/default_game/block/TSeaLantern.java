package net.hectus.neobb.turn.default_game.block;

import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.util.Colors;
import net.hectus.neobb.util.Modifiers;
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
        player.addModifier(Modifiers.P_REVIVE);
        player.sendMessage(Translation.component(player.locale(), "gameplay.info.revive.start").color(Colors.POSITIVE));
        player.game.turnScheduler.runTaskLater("revive", () -> {
            player.removeModifier(Modifiers.P_REVIVE);
            player.sendMessage(Translation.component(player.locale(), "gameplay.info.revive.end").color(Colors.NEGATIVE));
        }, 3);
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }
}
