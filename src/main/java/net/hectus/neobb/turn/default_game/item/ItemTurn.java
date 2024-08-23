package net.hectus.neobb.turn.default_game.item;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class ItemTurn extends Turn<ItemStack> {
    public ItemTurn(NeoPlayer player) { super(null, null, player); }
    public ItemTurn(ItemStack data, Location location, NeoPlayer player) { super(data, location, player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.valueOf(Utilities.camelToSnake(Utilities.counterFilterName(getClass().getSimpleName())).toUpperCase()));
    }
}
