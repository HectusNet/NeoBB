package net.hectus.neobb.turn.default_game.flower;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class TCornflower extends FlowerTurn {
    public TCornflower(NeoPlayer player) { super(player); }
    public TCornflower(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public void apply() {
        player.game.allowedClazzes().add(WaterClazz.class);
        player.game.world().setStorm(true);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.ALL, PotionEffectType.DARKNESS));
    }
}
