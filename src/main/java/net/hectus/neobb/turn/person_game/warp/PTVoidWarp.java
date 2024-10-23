package net.hectus.neobb.turn.person_game.warp;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import org.bukkit.World;
import org.bukkit.potion.PotionEffectType;

public class PTVoidWarp extends PWarpTurn {
    public PTVoidWarp(World world) { super(world, "void"); }
    public PTVoidWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "void"); }

    @Override
    public void apply() {
        super.apply();
        player.game.players().forEach(p -> p.inventory.removeRandom());
        new Buff.Effect(Buff.BuffTarget.OPPONENTS, PotionEffectType.BLINDNESS).apply(player);
        player.game.turnScheduler.runTaskLater("blindness", () -> // Blindness only lasts for 3 turns.
                player.opponents(true).forEach(p -> p.player.removePotionEffect(PotionEffectType.BLINDNESS)), 3);
    }

    @Override
    public int chance() {
        return 20;
    }
}
