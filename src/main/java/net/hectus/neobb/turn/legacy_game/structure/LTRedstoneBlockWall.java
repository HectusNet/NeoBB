package net.hectus.neobb.turn.legacy_game.structure;

import net.hectus.neobb.buff.Buff;
import net.hectus.neobb.player.NeoPlayer;
import net.hectus.neobb.structure.PlacedStructure;
import net.hectus.neobb.structure.Structure;
import net.hectus.neobb.structure.StructureManager;
import net.hectus.neobb.turn.default_game.attributes.clazz.RedstoneClazz;
import net.hectus.neobb.turn.default_game.attributes.function.BuffFunction;
import net.hectus.neobb.turn.default_game.structure.StructureTurn;
import net.hectus.neobb.util.Modifiers;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.List;

public class LTRedstoneBlockWall extends StructureTurn implements BuffFunction, RedstoneClazz {
    public LTRedstoneBlockWall(NeoPlayer player) { super(player); }
    public LTRedstoneBlockWall(PlacedStructure data, NeoPlayer player) { super(data, player); }

    @Override
    public int cost() {
        return 6;
    }

    @Override
    public Structure referenceStructure() {
        return StructureManager.structure("legacy-redstone_block_wall");
    }

    @Override
    public List<ItemStack> items() {
        return List.of(new ItemStack(Material.REDSTONE_BLOCK, 14));
    }

    @Override
    public void apply() {
        Player nextPlayer = player.nextPlayer().player;
        nextPlayer.setVelocity(nextPlayer.getVelocity().clone().add(new Vector(0, 2, 0)));

        player.game.addModifier(Modifiers.G_LEGACY_NO_ATTACK);
        player.game.turnScheduler.runTaskLater("redstone_block_wall", () -> {
            player.game.removeModifier(Modifiers.G_LEGACY_NO_ATTACK);
        }, 2);
    }

    @Override
    public List<Buff> buffs() {
        return List.of(new Buff.ExtraTurn(Buff.BuffTarget.YOU, 2));
    }
}
