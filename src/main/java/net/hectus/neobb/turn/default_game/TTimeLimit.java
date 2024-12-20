package net.hectus.neobb.turn.default_game;

import com.marcpg.libpg.data.time.Time;
import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.other.OtherTurn;
import net.hectus.neobb.util.Colors;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TTimeLimit extends OtherTurn<Time> implements NeutralClazz {
    public TTimeLimit(NeoPlayer player) { super(player); }
    public TTimeLimit(Time data, NeoPlayer player) { super(data, player.location(), player); }

    @Override
    public void apply() {
        player.sendMessage(Translation.component(player.locale(), "gameplay.info.too-slow.player", data.toString()).color(Colors.NEGATIVE));
        player.opponents(false).forEach(p -> p.sendMessage(Translation.component(p.locale(), "gameplay.info.too-slow." + (p == player.nextPlayer() ? "next" : "others"), player.nextPlayer()).color(Colors.NEUTRAL)));
    }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.CLOCK);
    }
}
