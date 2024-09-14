package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public abstract class HereTurn extends Turn<Block> {
    public HereTurn(NeoPlayer player) { super(player); }
    public HereTurn(Block data, NeoPlayer player) { super(data, data.getLocation(), player); }

    public abstract double damage();

    @Override
    public ItemStack item() {
        return new ItemStack(Material.valueOf(Utilities.camelToSnake(Utilities.counterFilterName(getClass().getSimpleName().replaceFirst("H", ""))).toUpperCase()));
    }

    @Override
    public Location location() {
        return location.clone().add(0.5, 0.5, 0.5);
    }

    @Override
    public void apply() {
        player.nextPlayer().damage(damage());
    }

    @Override
    public int cost() {
        return 0;
    }

    @Override
    public boolean goodChoice(NeoPlayer player) {
        return false; // Prevent the suggestion system from doing anything.
    }
}