package net.hectus.neobb.turn.legacy_game.structure;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.HotClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.NatureClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.WaterClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LTDaylightSensorStrip extends StructureTurn implements BuffFunction, CounterFunction, RedstoneClazz {
    public LTDaylightSensorStrip(NeoPlayer player) { super(player); }
    public LTDaylightSensorStrip(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("legacy-daylight_sensor_strip");
    }

    @Override
    public List<ItemStack> items() {
        return List.of(new ItemStack(Material.DAYLIGHT_DETECTOR, 7));
    }

    @Override
    public void apply() {
        player.game.world().setTime(1000); // 1000 should be midnight according to this: https://minecraft.wiki/w/Commands/time#Arguments
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.clazz(HotClazz.class), CounterFilter.clazz(NatureClazz.class), CounterFilter.clazz(WaterClazz.class), CounterFilter.clazz(RedstoneClazz.class));
    }
}
