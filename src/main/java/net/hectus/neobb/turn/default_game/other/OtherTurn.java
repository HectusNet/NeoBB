package net.hectus.neobb.turn.default_game.other;

import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.Turn;
import org.bukkit.Location;

public abstract class OtherTurn<T> extends Turn<T> {
    public OtherTurn(NeoPlayer player) { super(null, null, player); }
    public OtherTurn(T data, Location location, NeoPlayer player) { super(data, location, player); }
}
