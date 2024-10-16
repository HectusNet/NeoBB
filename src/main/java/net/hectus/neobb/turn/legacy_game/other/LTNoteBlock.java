package net.hectus.neobb.turn.legacy_game.other;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.CounterFilter;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterFunction;
import net.hectus.neobb.turn.default_game.attributes.function.CounterattackFunction;
import net.hectus.neobb.turn.default_game.block.TGoldBlock;
import net.hectus.neobb.turn.default_game.other.OtherTurn;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.NoteBlock;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LTNoteBlock extends OtherTurn<NoteBlock> implements CounterFunction, BuffFunction, RedstoneClazz {
    public LTNoteBlock(NeoPlayer player) { super(player); }
    public LTNoteBlock(NoteBlock data, Location location, NeoPlayer player) { super(data, location, player); }

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
        return List.of();
    }

    @Override
    public List<CounterFilter> counters() {
        return List.of(CounterFilter.of(t -> t.data() instanceof Block b && b.isSolid() && (!(t instanceof CounterattackFunction) || t instanceof TGoldBlock), "solid"));
    }

    public static int luck(@NotNull Instrument instrument) {
        return switch (instrument) {
            case BELL -> 35;
            case CHIME -> 30;
            case GUITAR, BANJO, COW_BELL -> 20;
            case DIDGERIDOO -> 15;
            case STICKS -> 10;
            case BASS_GUITAR -> 5;
            case BASS_DRUM -> -5;
            default -> -20;
        };
    }
}
