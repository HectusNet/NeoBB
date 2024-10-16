package net.hectus.neobb.turn.default_game.other;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.CounterbuffFunction;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TNoteBlock extends OtherTurn<NoteBlock> implements CounterbuffFunction, RedstoneClazz {
    public TNoteBlock(NeoPlayer player) { super(player); }
    public TNoteBlock(NoteBlock data, Location location, NeoPlayer player) { super(data, location, player); }

    @Override
    public int cost() {
        return 3;
    }

    @Override
    public void apply() {
        new Buff.Luck(Buff.BuffTarget.YOU, luck(data().getInstrument())).apply(player);
    }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.NOTE_BLOCK);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 10));
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(t -> t.data() instanceof Block b && b.isSolid(), "solid"));
    }

    public static int luck(@NotNull Instrument instrument) {
        return switch (instrument) {
            case BELL, CHIME -> 35;
            case COW_BELL, BANJO, GUITAR -> 20;
            case DIDGERIDOO -> 15;
            case STICKS -> 10;
            case BASS_DRUM, BASS_GUITAR -> 5;
            case PIANO -> -5;
            default -> 0;
        };
    }
}
