package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TDragonHead extends BlockTurn implements BuffFunction, SupernaturalClazz {
    public TDragonHead(NeoPlayer player) { super(player); }
    public TDragonHead(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.OPPONENTS, PotionEffectType.BLINDNESS), new Buff.Effect(Buff.BuffTarget.OPPONENTS, PotionEffectType.SLOWNESS), new Buff.Luck(Buff.BuffTarget.OPPONENTS, -20));
    }
}
