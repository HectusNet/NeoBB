package net.hectus.neobb.shop;

import com.marcpg.libpg.lang.Translation;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.util.Colors;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Shop {
    protected final Game game;
    public final List<Class<? extends Turn<?>>> turns;

    protected Shop(@NotNull Game game) {
        this.game = game;
        this.turns = game.info().turns();
    }

    public static Turn<?> turn(@NotNull Class<? extends Turn<?>> clazz, NeoPlayer player) {
        try {
            if (StructureManager.WARPS.contains(clazz)) {
                return clazz.getConstructor(World.class).newInstance(player.game.world());
            } else {
                return clazz.getConstructor(NeoPlayer.class).newInstance(player);
            }
        } catch (ReflectiveOperationException e) {
            NeoBB.LOG.warn("Couldn't get turn!", e);
            return Turn.DUMMY;
        }
    }

    public abstract void open(NeoPlayer player);

    public final void done(@NotNull NeoPlayer player) {
        player.inventory.setShopDone(true);
        if (game.allShopDone()) {
            game.start();
        } else {
            player.sendMessage(Translation.component(player.locale(), "shop.wait").color(Colors.EXTRA));
        }
    }
}
