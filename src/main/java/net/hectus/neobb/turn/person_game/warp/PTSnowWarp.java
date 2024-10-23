package net.hectus.neobb.turn.person_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.turn.person_game.categorization.Category;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.World;
import org.bukkit.entity.Snowman;

public class PTSnowWarp extends PWarpTurn implements Category {
    public PTSnowWarp(World world) { super(world, "snow"); }
    public PTSnowWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "snow"); }

    @Override
    public void apply() {
        super.apply();
        player.game.addModifier(Modifiers.G_PERSON_WHITE_ENTITIES);

        player.game.world().spawn(player.player.getLocation(), Snowman.class);
        player.game.addModifier(Modifiers.G_PERSON_SNOW_GOLEM);
        player.game.turnScheduler.runTaskTimer("snow_golem", () -> player.opponents(true).forEach(p -> p.damage(1.0)),
                () -> player.game.hasModifier(Modifiers.G_PERSON_SNOW_GOLEM), 1);
    }

    @Override
    public int chance() {
        return 40;
    }
}
