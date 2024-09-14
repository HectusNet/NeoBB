package net.hectus.neobb.turn.here_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class HTJackOLantern extends HereTurn {
    public HTJackOLantern(NeoPlayer player) { super(player); }
    public HTJackOLantern(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() {
        return player.game.world().isDayTime() ? 2 : 5;
    }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.JACK_O_LANTERN);
    }
}
