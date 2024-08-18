package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.MobUsage;
import org.bukkit.Material;
import org.bukkit.entity.Blaze;
import org.bukkit.inventory.ItemStack;

public class TBlaze extends Turn<Blaze> implements MobUsage, AttackFunction, HotClazz {
    public TBlaze(NeoPlayer player) { super(null, null, player); }
    public TBlaze(Blaze data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.BLAZE_SPAWN_EGG);
    }

    @Override
    public void apply() {
        player.game.addModifier("warp-prevent.cold");
        player.game.addModifier("warp-prevent.water"); // TODO: Remove all warp-prevent.* when warping!
    }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public Blaze getValue() {
        return data;
    }
}
