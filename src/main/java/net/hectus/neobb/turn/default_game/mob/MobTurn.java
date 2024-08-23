package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public abstract class MobTurn<T extends LivingEntity> extends Turn<T> {
    public MobTurn(NeoPlayer player) { super(null, null, player); }
    public MobTurn(T data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.valueOf(Utilities.camelToSnake(Utilities.counterFilterName(getClass().getSimpleName())).toUpperCase() + "_SPAWN_EGG"));
    }
}
