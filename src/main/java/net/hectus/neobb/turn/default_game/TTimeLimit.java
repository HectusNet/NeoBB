package net.hectus.neobb.turn.default_game;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.usage.OtherUsage;
import net.hectus.neobb.util.Colors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TTimeLimit extends Turn<Time> implements OtherUsage<Time>, NeutralClazz {
    public TTimeLimit(NeoPlayer player) { super(null, null, player); }
    public TTimeLimit(Time data, NeoPlayer player) { super(data, player.player.getLocation(), player); }


    @Override
    public void apply() {
        player.sendMessage(Translation.component(player.locale(), "gameplay.info.too-slow.player", data.toString()).color(Colors.NEGATIVE));
        player.opponents(false).forEach(p -> p.sendMessage(Translation.component(p.locale(), "gameplay.info.too-slow." + (p == player.nextPlayer() ? "next" : "others"), player.nextPlayer()).color(Colors.NEUTRAL)));
    }

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
