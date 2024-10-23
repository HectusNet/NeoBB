package net.hectus.neobb.turn.person_game.item;

import com.marcpg.libpg.storing.WeightedList;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.item.ItemTurn;
import net.hectus.neobb.turn.person_game.categorization.BuffCategory;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class PTSuspiciousStew extends ItemTurn implements BuffCategory {
    public static final WeightedList<Consumer<NeoPlayer>> BUFFS = new WeightedList<>();
    static {
        BUFFS.add(p -> p.inventory.removeRandom(), 10);
        BUFFS.add(p -> p.opponents(true).forEach(o -> o.inventory.removeRandom()), 10);
        BUFFS.add(p -> p.game.eliminatePlayer(p), 20);
        BUFFS.add(p -> p.heal(2), 10);
        BUFFS.add(p -> new Buff.ExtraTurn(Buff.BuffTarget.YOU, 2).apply(p), 10);
        BUFFS.add(p -> p.setElo(p.elo() + 2), 10);
        BUFFS.add(p -> p.setElo(p.elo() - 2), 10);
        BUFFS.add(p -> p.addLuck(15), 10);
        BUFFS.add(p -> p.addLuck(-15), 9);
        BUFFS.add(p -> p.game.win(p), 1);
    }

    public PTSuspiciousStew(NeoPlayer player) { super(player); }
    public PTSuspiciousStew(ItemStack data, Location location, NeoPlayer player) { super(data, location, player); }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }

    @Override
    public void apply() {
        super.apply();
        BUFFS.random().accept(player);
    }
}
