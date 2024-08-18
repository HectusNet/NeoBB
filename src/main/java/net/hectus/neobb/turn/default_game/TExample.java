package net.hectus.neobb.turn.default_game;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.usage.MobUsage;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

public class TExample extends Turn<LivingEntity> implements MobUsage {
    public TExample(NeoPlayer player) { super(null, null, player); }
    public TExample(LivingEntity data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.AIR);
    }

    @Override
    public int cost() {
        return 0;
    }

    @Override
    public LivingEntity getValue() {
        return data;
    }
}
