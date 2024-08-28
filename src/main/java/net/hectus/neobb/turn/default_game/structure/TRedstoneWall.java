package net.hectus.neobb.turn.default_game.structure;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.EventFunction;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TRedstoneWall extends StructureTurn implements EventFunction, RedstoneClazz {
    public TRedstoneWall(NeoPlayer player) { super(player); }
    public TRedstoneWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("redstone_wall");
    }

    @Override
    public List<ItemStack> items() {
        return List.of(new ItemStack(Material.REDSTONE_BLOCK, 6));
    }

    @Override
    public void triggerEvent() {
        player.game.addModifier("redstone_power"); // TODO: Power all blocks if this modifier is present.
        player.game.turnScheduler.runTaskLater("redstone_power", () -> player.game.removeModifier("redstone_power"), 3);
    }
}
