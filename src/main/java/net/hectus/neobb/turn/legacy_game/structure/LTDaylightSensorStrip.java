package net.hectus.neobb.turn.legacy_game.structure;

import com.marcpg.libpg.util.MinecraftTime;
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
    public void apply() {
        player.game.time(MinecraftTime.DAY);
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
