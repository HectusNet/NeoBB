package net.hectus.neobb.turn.person_game.other;

import com.marcpg.libpg.util.Randomizer;
import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.turn.default_game.other.OtherTurn;
import net.hectus.neobb.turn.person_game.categorization.BuffCategory;
import net.hectus.neobb.util.MinecraftTime;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Material;
import org.bukkit.entity.Painting;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.window.Window;

import java.util.List;

public class PTPainting extends OtherTurn<Painting> implements BuffCategory {
    public PTPainting(NeoPlayer player) { super(player); }
    public PTPainting(Painting data, NeoPlayer player) { super(data, data.getLocation(), player); }

    @Override
    public ItemStack item() {
        return new ItemStack(Material.PAINTING);
    }

    @Override
    public List<Buff> buffs() {
        return List.of();
    }

    @Override
    public void apply() {
        super.apply();
        switch (data.getArt()) {
            case ALBAN -> new Buff.Luck(Buff.BuffTarget.YOU, 30).apply(player);
            case AZTEC, AZTEC2 -> {
                player.inventory.removeRandom();
                player.inventory.removeRandom();
            }
            case BOMB -> player.game.players().forEach(p -> p.damage(5.0));
            case KEBAB -> player.inventory.removeRandom();
            case PLANT -> new Buff.ExtraTurn(Buff.BuffTarget.YOU, Randomizer.boolByChance(25) ? 2 : 1).apply(player);
            case WASTELAND -> player.game.players().forEach(p -> {
                p.inventory.clear();
                p.inventory.fillInRandomly();
            });
            case COURBET -> player.game.world().setTime(MinecraftTime.MIDNIGHT);
            case POOL -> player.addLuck(99);
            case SEA, BUST -> player.game.eliminatePlayer(player);
            case CREEBET -> {
                Gui.Builder.Normal gui = Gui.normal().setStructure("0 1 2 3 4 5 6 7 8");
                ItemStack[] deck = player.nextPlayer().inventory.deck();
                for (int i = 0; i < deck.length; i++)
                    gui.addIngredient((char) ('0' + i), deck[i]);

                Window.single().setTitle("Opponent Deck").setGui(gui.build()).open(player.player);
            }
            case SUNSET -> {
                List<NeoPlayer> players = player.game.players();
                double temp = players.getFirst().health();
                for (int i = 0; i < players.size() - 1; i++) {
                    players.get(i).health(players.get(i + 1).health());
                }
                players.getLast().health(temp);
            }
            case GRAHAM -> player.addModifier(Modifiers.P_NO_MOVE);
            case WANDERER -> player.game.addModifier(Modifiers.G_PERSON_NO_WIN_CONS);
            case MATCH -> player.addLuck(20);
            case SKULL_AND_ROSES -> player.game.arena.clear();
            case STAGE -> player.opponents(true).forEach(p -> {
                p.player.getLocation().getBlock().setType(Material.COBWEB);
                if (Randomizer.boolByChance(20))
                    p.game.eliminatePlayer(p);
            });
            case VOID -> {
                new Buff.ExtraTurn().apply(player);
                player.addArmor(1);
            }
            case WITHER -> player.game.eliminatePlayer(Randomizer.boolByChance(20) ? player.nextPlayer() : player);
        }
    }
}
