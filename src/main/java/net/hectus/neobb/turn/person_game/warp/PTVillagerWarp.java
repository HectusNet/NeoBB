package net.hectus.neobb.turn.person_game.warp;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.World;

public class PTVillagerWarp extends PWarpTurn {
    public PTVillagerWarp(World world) { super(world, "villager"); }
    public PTVillagerWarp(PlacedStructure data, World world, NeoPlayer player) { super(data, world, player, "villager"); }

    @Override
    public void apply() {
        super.apply();
        player.game.addModifier(Modifiers.G_PERSON_VILLAGER_ENTITIES);
        player.inventory.addRandom();
    }

    @Override
    public int chance() {
        return 40;
    }
}
