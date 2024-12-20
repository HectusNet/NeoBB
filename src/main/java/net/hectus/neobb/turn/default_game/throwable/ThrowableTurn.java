package net.hectus.neobb.turn.default_game.throwable;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

public abstract class ThrowableTurn extends Turn<Projectile> {
    public ThrowableTurn(NeoPlayer player) { super(player); }
    public ThrowableTurn(Projectile data, Location impact, NeoPlayer player) { super(data, impact, player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.valueOf(Utilities.camelToSnake(Utilities.counterFilterName(getClass().getSimpleName())).toUpperCase()));
    }
}
