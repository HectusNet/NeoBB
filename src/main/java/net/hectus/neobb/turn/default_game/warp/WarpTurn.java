package net.hectus.neobb.turn.default_game.warp;

import com.marcpg.libpg.storing.Cord;
import com.marcpg.libpg.storing.CordMinecraftAdapter;
import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.attributes.clazz.Clazz;
import net.hectus.neobb.turn.default_game.attributes.function.WarpFunction;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.hectus.neobb.util.Configuration;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;

public abstract class WarpTurn extends StructureTurn implements WarpFunction {
    public final Location center;
    public final String name;

    public WarpTurn(World world, String name) {
        super(null);
        this.cord = Cord.ofList(Configuration.CONFIG.getIntegerList("warps." + name));
        this.name = name;
        this.center = CordMinecraftAdapter.toLocation(cord, world).add(5, 0, 5);
    }

    public WarpTurn(PlacedStructure data, World world, NeoPlayer player, String name) {
        super(data, player);
        this.cord = Cord.ofList(Configuration.CONFIG.getIntegerList("warps." + name));
        this.name = name;
        this.center = CordMinecraftAdapter.toLocation(cord, world).add(5, 0, 5);
    }

    public abstract int chance();
    public abstract List<Class<? extends Clazz>> allows();

    public boolean canBePlayed() {
        return true;
    }

    public enum Temperature { COLD, NORMAL, HOT }
    public Temperature temperature() {
        return switch (allows().getFirst().getSimpleName()) {
            case "ColdClazz" -> Temperature.COLD;
            case "HotClazz" -> Temperature.HOT;
            default -> Temperature.NORMAL;
        };
    }

    public Cord highCorner() {
        return cord.add(new Cord(9, Configuration.MAX_ARENA_HEIGHT, 9));
    }

    @Override
    public boolean requiresUsageGuide() {
        return true;
    }

    @Override
    public int maxAmount() {
        return 1;
    }

    @Override
    public void apply() {
        if (canBePlayed() && (Randomizer.boolByChance(chance()) || player.hasModifier(Modifiers.P_DEFAULT_100P_WARP))) {
            player.removeModifier(Modifiers.P_DEFAULT_100P_WARP);
            player.game.players().forEach(p -> p.teleport(p.cord().subtract(player.game.warp().cord()).add(cord)));
            player.game.warp(this);
        }
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure(name + "-warp");
    }
}
