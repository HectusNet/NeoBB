package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TSunflower extends FlowerTurn {
    public TSunflower(NeoPlayer player) { super(player); }
    public TSunflower(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public void apply() {
        player.game.world().setClearWeatherDuration(Integer.MAX_VALUE);
        player.game.world().setStorm(false);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 10), new Buff.Effect(Buff.BuffTarget.ALL, PotionEffectType.SPEED, 1));
    }
}
