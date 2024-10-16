package net.hectus.neobb.turn.legacy_game.item;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.turn.default_game.block.TLightningRod;
import net.hectus.neobb.turn.default_game.item.ItemTurn;
import net.hectus.neobb.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LightningStrike;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class LTLightningTrident extends ItemTurn implements CounterFunction, BuffFunction, NeutralClazz {
    public LTLightningTrident(NeoPlayer player) { super(player); }
    public LTLightningTrident(ItemStack data, Location location, NeoPlayer player) { super(data, location, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public ItemStack item() {
        return new ItemBuilder(Material.TRIDENT).name(Component.text("Lightning Trident")).build();
    }

    @Override
    public void apply() {
        player.game.world().spawn(location(), LightningStrike.class);
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(t -> t instanceof TLightningRod, "lightning_rod"));
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Effect(Buff.BuffTarget.YOU, PotionEffectType.GLOWING), new Buff.Effect(Buff.BuffTarget.OPPONENTS, PotionEffectType.BLINDNESS));
    }
}
