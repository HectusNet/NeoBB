package net.hectus.bb.warp;

import com.marcpg.libpg.color.McFormat;
import com.marcpg.libpg.lang.Translatable;
import com.marcpg.libpg.lang.Translation;
import net.hectus.bb.BlockBattles;
import net.hectus.bb.game.Game;
import net.hectus.bb.turn.Turn;
import net.hectus.bb.util.Parser;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static net.hectus.bb.turn.Turn.ItemClass.*;

public enum Warp implements Translatable {
    DEFAULT(1.0, Temperature.MEDIUM, WATER_CLASS, NATURE),
    NETHER(0.8, Temperature.WARM, Turn.ItemClass.REDSTONE),
    END(0.6, Temperature.COLD, SUPERNATURAL),
    OCEAN(0.7, Temperature.MEDIUM, WATER_CLASS),
    FROZEN(0.7, Temperature.COLD, WATER_CLASS),
    MUSHROOM(0.5, Temperature.MEDIUM, NATURE, SUPERNATURAL),
    CLIFF(0.6, Temperature.MEDIUM, Turn.ItemClass.REDSTONE),
    MEADOW(0.3, Temperature.MEDIUM, NATURE, SUPERNATURAL),
    VOID(0.1, Temperature.COLD, NEUTRAL, WATER_CLASS, Turn.ItemClass.REDSTONE, SUPERNATURAL),
    REDSTONE(0.6, Temperature.WARM, Turn.ItemClass.REDSTONE),
    WOOD(1.0, Temperature.MEDIUM, NATURE),
    DESERT(0.8, Temperature.WARM, Turn.ItemClass.REDSTONE),
    NERD(0.2, Temperature.MEDIUM, NATURE, Turn.ItemClass.REDSTONE, SUPERNATURAL),
    AMETHYST(0.2, Temperature.MEDIUM, WATER_CLASS, NATURE, SUPERNATURAL),
    SUN(0.4, Temperature.WARM, Turn.ItemClass.REDSTONE, SUPERNATURAL);

    public final double chance;
    public final Temperature temperature;
    public final List<Turn.ItemClass> allowed;

    Warp(double chance, @NotNull Temperature temperature, Turn.ItemClass... allowed) {
        this.chance = chance;
        this.temperature = temperature;
        this.allowed = new ArrayList<>(List.of(OTHER, temperature.itemClass()));
        this.allowed.addAll(List.of(allowed));
    }

    public void teleport(@NotNull Game game) {
        Location location = Parser.listToLocation(BlockBattles.CONFIG.getIntegerList("warps." + name().toLowerCase()));
        game.turning().player().teleport(location.clone().add(2, 0, 0));
        game.getOpponent(game.turning()).player().teleport(location.clone().subtract(2, 0, 0));
    }

    @Override
    public @NotNull String getTranslated(Locale locale) {
        return Translation.string(locale, "warp." + name().toLowerCase());
    }

    public enum Temperature {
        COLD, MEDIUM, WARM;

        public McFormat mcFormat() {
            return switch (this) {
                case COLD -> McFormat.BLUE;
                case MEDIUM -> McFormat.RESET;
                case WARM -> McFormat.RED;
            };
        }

        public Turn.ItemClass itemClass() {
            return switch (this) {
                case COLD -> Turn.ItemClass.COLD;
                case MEDIUM -> NEUTRAL;
                case WARM -> HOT;
            };
        }
    }
}
