package net.hectus.neobb.turn.person_game.other;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.other.OtherTurn;
import net.hectus.neobb.turn.person_game.categorization.DefensiveCounterCategory;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

public class PTArmorStand extends OtherTurn<ArmorStand> implements DefensiveCounterCategory {
    public PTArmorStand(NeoPlayer player) { super(player); }
    public PTArmorStand(ArmorStand data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.ARMOR_STAND);
    }
}
