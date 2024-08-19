package net.hectus.neobb.turn.default_game.other;

import com.marcpg.libpg.data.time.Time;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.usage.OtherUsage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TTimeLimit extends Turn<Time> implements OtherUsage<Time>, NeutralClazz {
    public TTimeLimit(NeoPlayer player) { super(null, null, player); }
    public TTimeLimit(Time data, NeoPlayer player) { super(data, player.player.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.CLOCK);
    }

    @Override
    public int cost() {
        return 0;
    }

    @Override
    public Time getValue() {
        return data;
    }
}
