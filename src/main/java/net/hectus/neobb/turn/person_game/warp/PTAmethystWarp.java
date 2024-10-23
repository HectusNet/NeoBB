package net.hectus.neobb.turn.person_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import org.bukkit.World;

public class PTAmethystWarp extends PWarpTurn {
    public PTAmethystWarp(World world) { super(world, "amethyst"); }
    public PTAmethystWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "amethyst"); }

    @Override
    public void apply() {
        super.apply();
        player.health(player.game.info().startingHealth());
    }

    @Override
    public double damage() {
        return 1.0;
    }

    @Override
    public int chance() {
        return 10;
    }
}
