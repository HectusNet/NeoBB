package net.hectus.neobb.structure;

import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

public class PlacedStructure extends Structure {
    protected final Block lastBlock;

    public PlacedStructure(String name, BlockInfo @NotNull [][][] blocks, Block lastBlock) {
        super(name, blocks);
        this.lastBlock = lastBlock;
    }

    public PlacedStructure(@NotNull Structure structure, Block lastBlock) {
        this(structure.name, structure.blocks, lastBlock);
    }

    public Block lastBlock() {
        return lastBlock;
    }
}
