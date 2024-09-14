package net.hectus.neobb.turn.default_game.other;

import net.hectus.neobb.NeoBB;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.EventFunction;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Boat;
import org.bukkit.inventory.ItemStack;

public class TBoat extends OtherTurn<Boat> implements EventFunction, WaterClazz {
    public TBoat(NeoPlayer player) { super(player); }
    public TBoat(Boat data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public boolean requiresUsageGuide() { return true; }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.OAK_BOAT);
    }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public void triggerEvent() {
        player.nextPlayer().addModifier(Modifiers.P_DEFAULT_BOAT_DAMAGE);
        Bukkit.getScheduler().runTaskTimer(NeoBB.PLUGIN, r -> {
            if (player.nextPlayer().hasModifier(Modifiers.P_DEFAULT_BOAT_DAMAGE)) {
                player.nextPlayer().player.damage(1.0);
            } else {
                r.cancel();
            }
        }, 0, 20);
    }
}
