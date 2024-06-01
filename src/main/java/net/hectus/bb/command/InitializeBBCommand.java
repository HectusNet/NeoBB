package net.hectus.bb.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class InitializeBBCommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        long start = System.nanoTime();
        sender.sendMessage(Component.text("Populating Server with necessary Settings!", NamedTextColor.YELLOW));

        sender.sendMessage(Component.text("At server: " + Bukkit.getName(), NamedTextColor.WHITE));
        sender.sendMessage(Component.text("gameMode=survival", NamedTextColor.DARK_GRAY));
        Bukkit.setDefaultGameMode(GameMode.SURVIVAL);
        sender.sendMessage(Component.text("spawnRadius=0", NamedTextColor.DARK_GRAY));
        Bukkit.setSpawnRadius(0);

        for (World world : Bukkit.getWorlds()) {
            sender.sendMessage(Component.text("At world: " + world.getName(), NamedTextColor.WHITE));

            sender.sendMessage(Component.text("Settings general world/server settings for world " + world.getName(), NamedTextColor.GRAY));
            sender.sendMessage(Component.text("autoSave=false", NamedTextColor.DARK_GRAY));
            world.setAutoSave(false);
            sender.sendMessage(Component.text("difficulty=normal", NamedTextColor.DARK_GRAY));
            world.setDifficulty(Difficulty.NORMAL);
            sender.sendMessage(Component.text("hardcore=false", NamedTextColor.DARK_GRAY));
            world.setHardcore(false);
            sender.sendMessage(Component.text("weather=clear", NamedTextColor.DARK_GRAY));
            world.setPVP(false);
            sender.sendMessage(Component.text("weather=clear", NamedTextColor.DARK_GRAY));
            world.setStorm(false);

            sender.sendMessage(Component.text("Settings game rules for world " + world.getName(), NamedTextColor.GRAY));
            sender.sendMessage(Component.text("announceAdvancements=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            sender.sendMessage(Component.text("doDaylightCycle=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            sender.sendMessage(Component.text("doEntityDrops=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
            sender.sendMessage(Component.text("doFireTick=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            sender.sendMessage(Component.text("doLimitedCrafting=true", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_LIMITED_CRAFTING, true);
            sender.sendMessage(Component.text("doMobLoot=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_MOB_LOOT, false);
            sender.sendMessage(Component.text("doMobSpawning=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            sender.sendMessage(Component.text("doTileDrops=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_TILE_DROPS, false);
            sender.sendMessage(Component.text("doWeatherCycle=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            sender.sendMessage(Component.text("keepInventory=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.KEEP_INVENTORY, false);
            sender.sendMessage(Component.text("mobGriefing=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.MOB_GRIEFING, false);
            sender.sendMessage(Component.text("naturalGeneration=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
            sender.sendMessage(Component.text("showDeathMessages=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);
            sender.sendMessage(Component.text("spectatorsGenerateChunks=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
            sender.sendMessage(Component.text("disableRaids=true", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DISABLE_RAIDS, true);
            sender.sendMessage(Component.text("doImmediateRespawn=true", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true);
            sender.sendMessage(Component.text("doPatrolSpawning=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
            sender.sendMessage(Component.text("doTraderSpawning=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
            sender.sendMessage(Component.text("doWardenSpawning=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
            sender.sendMessage(Component.text("blockExplosionDropDecay=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.BLOCK_EXPLOSION_DROP_DECAY, false);
            sender.sendMessage(Component.text("mobExplosionDropDecay=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.MOB_EXPLOSION_DROP_DECAY, false);
            sender.sendMessage(Component.text("tntExplosionDropDecay=false", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.TNT_EXPLOSION_DROP_DECAY, false);
            sender.sendMessage(Component.text("spawnRadius=0", NamedTextColor.DARK_GRAY));
            world.setGameRule(GameRule.SPAWN_RADIUS, 0);
        }

        sender.sendMessage(Component.text("Done! (Took " + (System.nanoTime() - start) + "ns)", NamedTextColor.GREEN));
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
