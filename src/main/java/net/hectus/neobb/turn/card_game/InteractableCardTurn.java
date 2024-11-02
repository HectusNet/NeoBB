package net.hectus.neobb.turn.card_game;

import net.hectus.neobb.player.NeoPlayer;
import org.bukkit.block.Block;

public abstract class InteractableCardTurn extends CardTurn {
    private int interactions = 0;
    private long lastInteraction = System.currentTimeMillis();

    public InteractableCardTurn(NeoPlayer player) { super(player); }
    public InteractableCardTurn(Block data, NeoPlayer player) { super(data, player); }

    public int maxInteractions() { return 3; }
    public long interactionIntervalMs() { return 1000; }

    public synchronized void interact() {
        if (interactions <= maxInteractions() && System.currentTimeMillis() - lastInteraction <= interactionIntervalMs()) {
            lastInteraction = System.currentTimeMillis();
            interactions++;
            player.nextPlayer().damage(damage());
        }
    }
}
