package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.MobUsage;
import org.bukkit.Material;
import org.bukkit.entity.Phantom;
import org.bukkit.inventory.ItemStack;

public class TPhantom extends Turn<Phantom> implements MobUsage, AttackFunction, SupernaturalClazz {
    public TPhantom(NeoPlayer player) { super(null, null, player); }
    public TPhantom(Phantom data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.PHANTOM_SPAWN_EGG);
    }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public Phantom getValue() {
        return data;
    }
}
