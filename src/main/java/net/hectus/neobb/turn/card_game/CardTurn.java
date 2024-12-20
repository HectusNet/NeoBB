package net.hectus.neobb.turn.card_game;

import com.marcpg.libpg.storing.Cord;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class CardTurn extends Turn<Block> {
    public CardTurn(NeoPlayer player) { super(player); }
    public CardTurn(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public abstract double damage(); // Change it to be abstract instead of concrete.

    @Override
    public ItemStack item() {
        return new ItemStack(Material.valueOf(Utilities.camelToSnake(Utilities.counterFilterName(getClass().getSimpleName())).toUpperCase()));
    }

    @Override
    public Cord cord() {
        return cord.add(new Cord(0.5, 0.5, 0.5));
    }

    @Override
    public boolean goodChoice(@NotNull NeoPlayer player) {
        return false; // Prevent the suggestion system from doing anything.
    }
}
