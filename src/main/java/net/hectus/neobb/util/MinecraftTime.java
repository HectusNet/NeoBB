package net.hectus.neobb.util;

/**
 * Source: <a href="https://minecraft.wiki/w/Commands/time#Arguments">Minecraft Wiki / Time</a>
 */
@SuppressWarnings("unused")
public enum MinecraftTime {
    DAY(1000),
    NOON(6000),
    SUNSET(12000),
    NIGHT(13000),
    MIDNIGHT(18000),
    SUNRISE(23000);

    public final int time;

    MinecraftTime(int time) {
        this.time = time;
    }
}
