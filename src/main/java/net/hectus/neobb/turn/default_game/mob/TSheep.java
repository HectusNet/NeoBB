package net.hectus.neobb.turn.default_game.mob;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.attributes.clazz.ColdClazz;
import net.hectus.neobb.turn.default_game.attributes.clazz.SupernaturalClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import org.bukkit.entity.Sheep;

import java.util.List;

public class TSheep extends MobTurn<Sheep> implements BuffFunction, SupernaturalClazz {
    public TSheep(NeoPlayer player) { super(player); }
    public TSheep(Sheep data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 4;
    }

    @Override
    public void apply() {
        switch (data.getColor()) {
            case PINK -> player.game.win(player);
            case LIGHT_GRAY -> new Buff.ExtraTurn().apply(player);
            case GRAY -> new Buff.Luck(Buff.BuffTarget.YOU, 25).apply(player);
            case BLACK -> new Buff.Luck(Buff.BuffTarget.OPPONENTS, -15).apply(player);
            case BROWN -> player.inventory.addRandom();
            case null, default -> {
                if (player.game.allowedClazzes().contains(ColdClazz.class))
                    new Buff.ExtraTurn().apply(player);
            }
        }
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.Luck(Buff.BuffTarget.YOU, 5));
    }
}
