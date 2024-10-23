package net.hectus.neobb.turn.person_game.categorization;

import org.bukkit.Material;

public interface SituationalAttackCategory extends AttackCategory {
    @Override
    default int categoryMaxPerDeck() {
        return 1;
    }

    @Override
    default Material categoryItem() {
        return Material.SNOWBALL;
    }

    @Override
    default String categoryName() {
        return "situational_attack";
    }
}
