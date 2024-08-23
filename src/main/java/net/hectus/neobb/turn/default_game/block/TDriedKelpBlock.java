package net.hectus.neobb.turn.default_game.block;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TDriedKelpBlock extends BlockTurn implements BuffFunction, WaterClazz {
    public TDriedKelpBlock(NeoPlayer player) { super(player); }
    public TDriedKelpBlock(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.YOU, PotionEffectType.JUMP_BOOST, 2));
    }
}
