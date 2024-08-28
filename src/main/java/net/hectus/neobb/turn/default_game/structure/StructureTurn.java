package net.hectus.neobb.turn.default_game.structure;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.turn.Turn;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class StructureTurn extends Turn<PlacedStructure> {
    public StructureTurn(NeoPlayer player) { super(null, null, player); }
    public StructureTurn(PlacedStructure data, NeoPlayer player) { super(data, data.lastBlock().getLocation(), player); }

    public abstract Structure referenceStructure();

    public abstract List<ItemStack> items();
}
