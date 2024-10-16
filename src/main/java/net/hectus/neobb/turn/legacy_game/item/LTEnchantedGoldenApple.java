package net.hectus.neobb.turn.legacy_game.item;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.buff.ChancedBuff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.turn.default_game.item.ItemTurn;
import net.hectus.neobb.turn.default_game.mob.MobTurn;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LTEnchantedGoldenApple extends ItemTurn implements CounterFunction, BuffFunction, NeutralClazz {
    public LTEnchantedGoldenApple(NeoPlayer player) { super(player); }
    public LTEnchantedGoldenApple(ItemStack data, Location location, NeoPlayer player) { super(data, location, player); }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new ChancedBuff(new Buff.Luck(Buff.BuffTarget.YOU, 100), 50));
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(t -> t instanceof MobTurn<?>, "mobs"));
    }
}
