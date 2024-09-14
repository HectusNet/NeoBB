package net.hectus.neobb.turn.default_game.item;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.EventFunction;
import net.hectus.neobb.util.Utilities;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class TChorusFruit extends ItemTurn implements EventFunction, NeutralClazz {
    public TChorusFruit(NeoPlayer player) { super(player); }
    public TChorusFruit(ItemStack data, Location location, NeoPlayer player) { super(data, location, player); }

    @Override
    public int cost() {
        return 2;
    }

    @Override
    public void triggerEvent() {
        Location loc = player.game.warp().location().clone().add(Utilities.RANDOM.nextDouble(0.0, 9.0), 0, Utilities.RANDOM.nextDouble(0.0, 9.0));
        loc.setPitch(Utilities.RANDOM.nextFloat(-90.0f, 90.0f));
        loc.setYaw(Utilities.RANDOM.nextFloat(0.0f, 360.0f));
        player.player.teleport(loc);
    }
}
