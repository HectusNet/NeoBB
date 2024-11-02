package net.hectus.neobb.shop;

import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.lore.ItemLoreBuilder;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Colors;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Shop {
    public final NeoPlayer player;
    public final ItemLoreBuilder loreBuilder;

    public final List<? extends Turn<?>> dummyTurns;

    public Shop(@NotNull NeoPlayer player) {
        this.player = player;
        this.dummyTurns = player.game.info().turns().stream().map(t -> turn(t, player)).toList();
        try {
            this.loreBuilder = player.game.info().loreBuilder().getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Turn<?> turn(@NotNull Class<? extends Turn<?>> clazz, NeoPlayer player) {
        try {
            if (clazz.getName().toLowerCase().contains("warp")) {
                return clazz.getConstructor(World.class).newInstance(player.game.world());
            } else {
                return clazz.getConstructor(NeoPlayer.class).newInstance(player);
            }
        } catch (ReflectiveOperationException e) {
            NeoBB.LOG.warn("Could not get turn!", e);
            return Turn.DUMMY;
        }
    }

    public abstract void open();

    public final void done() {
        player.inventory.setShopDone(true);
        if (player.game.allShopDone()) {
            player.game.start();
        } else {
            player.sendMessage(Translation.component(player.locale(), "shop.wait").color(Colors.EXTRA));
        }
    }
}
