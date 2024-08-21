package net.hectus.neobb.turn.default_game.mob;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.Turn;
import net.hectus.neobb.turn.default_game.attributes.clazz.*;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.usage.MobUsage;
import net.hectus.neobb.turn.default_game.warp.Warp;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Axolotl;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TAxolotl extends Turn<Axolotl> implements MobUsage, BuffFunction, WaterClazz {
    public TAxolotl(NeoPlayer player) { super(null, null, player); }
    public TAxolotl(Axolotl data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.AXOLOTL_SPAWN_EGG);
    }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public Axolotl getValue() {
        return data;
    }

    @Override
    public void apply() {
        switch (data.getVariant()) {
            case LUCY -> randomWarp(HotClazz.class); // Pink
            case WILD -> randomWarp(NatureClazz.class); // Brown
            case GOLD -> randomWarp(SupernaturalClazz.class); // Gold / Yellow
            case CYAN -> randomWarp(WaterClazz.class); // Cyan
            case BLUE -> player.game.win(player); // Blue
        }
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 10), new Buff.Luck(Buff.BuffTarget.OPPONENTS, -10));
    }

    private void randomWarp(Class<? extends Clazz> allows) {
        player.game.warp(Randomizer.fromCollection(StructureManager.WARPS.stream()
                .map(this::classToWarp)
                .filter(w -> w != null && w.allows().contains(allows))
                .toList()));
    }

    private @Nullable Warp classToWarp(@NotNull Class<? extends Warp> warpClass) {
        try {
            return warpClass.getConstructor(World.class).newInstance(player.game.world());
        } catch (ReflectiveOperationException ignored) {
            return null;
        }
    }
}
