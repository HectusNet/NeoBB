package net.hectus.neobb.turn.default_game.structure;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NeutralClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.turn.default_game.attributes.function.DefenseFunction;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class TOakDoorTurtling extends StructureTurn implements DefenseFunction, NeutralClazz {
    public TOakDoorTurtling(NeoPlayer player) { super(player); }
    public TOakDoorTurtling(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("oak_door_turtling");
    }

    @Override
    public List<ItemStack> items() {
        // Not sure if it would be OAK_DOOR or some lower part thing with the automatic item detection based on the saved structure.
        return List.of(new ItemStack(Material.OAK_DOOR, 4));
    }

    @Override
    public void applyDefense() {
        player.addModifier(Modifiers.P_DEFAULT_DEFENDED);
        player.game.turnScheduler.runTaskTimer("oak_door_turtling", () -> {
            if (player.game.history().getLast() instanceof CounterFunction &&
                    CounterFilter.clazz(HotClazz.class).doCounter(player.game.history().getLast()) ||
                    CounterFilter.clazz(RedstoneClazz.class).doCounter(player.game.history().getLast())) {
                player.removeModifier(Modifiers.P_DEFAULT_DEFENDED);
            }
        }, () -> !player.hasModifier(Modifiers.P_DEFAULT_DEFENDED), 1);
    }
}
