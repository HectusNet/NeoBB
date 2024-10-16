package net.hectus.neobb.turn.legacy_game.structure;

import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.Game;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.DummyTurn;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.function.AttackFunction;
import net.hectus.neobb.turn.default_game.other.OtherTurn;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LTNetherPortal extends OtherTurn<List<BlockState>> implements HotClazz, AttackFunction, DummyTurn {
    public LTNetherPortal(NeoPlayer player) { super(player); }
    public LTNetherPortal(List<BlockState> data, Location location, NeoPlayer player) { super(data, location, player); }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public List<ItemStack> items() {
        return List.of(new ItemStack(Material.OBSIDIAN, 14));
    }

    public static void await(@NotNull Game game) {
        game.addModifier(Modifiers.G_LEGACY_PORTAL_AWAIT);
        Bukkit.getScheduler().runTaskLater(NeoBB.PLUGIN, () -> {
            if (game.hasModifier(Modifiers.G_LEGACY_PORTAL_AWAIT)) {
                game.removeModifier(Modifiers.G_LEGACY_PORTAL_AWAIT);
                game.draw(false);
            }
        }, 100); // 5 Seconds
    }
}
