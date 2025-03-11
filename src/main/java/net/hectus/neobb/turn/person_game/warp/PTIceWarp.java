package net.hectus.neobb.turn.person_game.warp;

import com.marcpg.libpg.util.MinecraftTime;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.World;

public class PTIceWarp extends PWarpTurn {
    public PTIceWarp(World world) { super(world, "ice"); }
    public PTIceWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "ice"); }

    @Override
    public void apply() {
        super.apply();
        player.game.addModifier(Modifiers.G_PERSON_BLUE_ENTITIES);
        player.game.turnScheduler.runTaskTimer("icing", () -> player.game.players().forEach(p -> p.damage(1.0)), () -> true, 1);
        player.game.time(MinecraftTime.NOON);
    }

    @Override
    public int chance() {
        return 50;
    }
}
