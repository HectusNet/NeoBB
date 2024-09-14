package net.hectus.neobb.turn.default_game.structure.glass_wall;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.function.DefenseFunction;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class GlassWallTurn extends StructureTurn implements DefenseFunction, BuffFunction {
    protected boolean stay;

    public GlassWallTurn(NeoPlayer player) { super(player); }
    public GlassWallTurn(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 5;
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure(color().toLowerCase() + "_glass_wall");
    }

    @Override
    public List<ItemStack> items() {
        return List.of(new ItemStack(Material.valueOf(color().toUpperCase() + "_STAINED_GLASS"), 6));
    }

    public abstract String color();

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 5));
    }

    @Override
    public void applyDefense() {
        player.addModifier(Modifiers.P_DEFAULT_DEFENDED);
        player.game.turnScheduler.runTaskTimer("glass_wall_defense", () -> {
            stay = Randomizer.boolByChance(40);
            if (stay) player.addModifier(Modifiers.P_DEFAULT_DEFENDED);
        }, () -> stay, 1);
    }
}
