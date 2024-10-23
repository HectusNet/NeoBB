package net.hectus.neobb.turn.person_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.util.MinecraftTime;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.World;

public class PTFireWarp extends PWarpTurn {
    public PTFireWarp(World world) { super(world, "fire"); }
    public PTFireWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "fire"); }

    @Override
    public void apply() {
        super.apply();
        player.game.addModifier(Modifiers.G_PERSON_FIRE_ENTITIES);
        player.game.turnScheduler.runTaskTimer("burning", () -> player.game.players().forEach(p -> p.damage(1.0)), () -> true, 1);
        player.game.world().setTime(MinecraftTime.NOON);
    }

    @Override
    public int chance() {
        return 50;
    }
}
