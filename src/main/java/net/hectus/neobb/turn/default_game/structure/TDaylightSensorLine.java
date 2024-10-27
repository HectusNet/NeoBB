package net.hectus.neobb.turn.default_game.structure;

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
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import net.hectus.neobb.util.MinecraftTime;

import java.util.List;

public class TDaylightSensorLine extends StructureTurn implements CounterbuffFunction, RedstoneClazz {
    public TDaylightSensorLine(NeoPlayer player) { super(player); }
    public TDaylightSensorLine(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("daylight_sensor_line");
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
