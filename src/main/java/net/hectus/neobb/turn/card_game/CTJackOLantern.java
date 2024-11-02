package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.util.MinecraftTime;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class CTJackOLantern extends CardTurn {
    public CTJackOLantern(NeoPlayer player) { super(player); }
    public CTJackOLantern(Block data, NeoPlayer player) { super(data, player); }

    @Override
    public double damage() {
        return player.game.time() == MinecraftTime.MIDNIGHT ? 5 : 2;
    }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.JACK_O_LANTERN);
    }
}
