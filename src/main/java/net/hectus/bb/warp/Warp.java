package net.hectus.bb.warp;

public enum Warp {
    DEFAULT(1.0, Temperature.MEDIUM),
    NETHER(0.8, Temperature.WARM),
    END(0.6, Temperature.COLD),
    OCEAN(0.7, Temperature.MEDIUM),
    FROZEN(0.7, Temperature.COLD),
    MUSHROOM(0.5, Temperature.MEDIUM),
    CLIFF(0.6, Temperature.MEDIUM),
    MEADOW(0.3, Temperature.MEDIUM),
    VOID(0.1, Temperature.COLD),
    REDSTONE(0.6, Temperature.MEDIUM),
    WOOD(1.0, Temperature.MEDIUM),
    DESERT(0.8, Temperature.WARM),
    NERD(0.2, Temperature.MEDIUM),
    AMETHYST(0.2, Temperature.MEDIUM),
    SUN(0.4, Temperature.WARM);

    public final double chance;
    public final Temperature temperature;

    Warp(double chance, Temperature temperature) {
        this.chance = chance;
        this.temperature = temperature;
    }

    public enum Temperature { COLD, MEDIUM, WARM }
}
