package net.hectus.neobb.turn.default_game.mob;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.warp.WarpTurn;
import org.bukkit.entity.Axolotl;

import java.util.List;

public class TAxolotl extends MobTurn<Axolotl> implements BuffFunction, WaterClazz {
    public TAxolotl(NeoPlayer player) { super(player); }
    public TAxolotl(Axolotl data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public void apply() {
        switch (data.getVariant()) {
            case LUCY -> randomWarp(HotClazz.class); // Pink
            case WILD -> randomWarp(NatureClazz.class); // Brown
            case GOLD -> randomWarp(SupernaturalClazz.class); // Gold / Yellow
            case CYAN -> randomWarp(WaterClazz.class); // Cyan
            case BLUE -> player.game.win(player); // Blue
        }
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 10), new Buff.Luck(Buff.BuffTarget.OPPONENTS, -10));
    }

    private void randomWarp(Class<? extends Clazz> allows) {
        player.game.warp(Randomizer.fromCollection(player.shop.dummyTurns.stream()
                .filter(t -> t instanceof WarpTurn w && w.allows().contains(allows))
                .map(t -> (WarpTurn) t)
                .toList()));
    }
}
