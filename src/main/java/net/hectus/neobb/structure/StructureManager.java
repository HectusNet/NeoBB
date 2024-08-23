package net.hectus.neobb.structure;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.util.Arena;
import net.hectus.neobb.turn.default_game.warp.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public final class StructureManager {
    private static final List<Structure> STRUCTURES = new ArrayList<>();
    private static final Set<Material> STRUCTURE_MATERIALS = new HashSet<>();

    public static final List<Class<? extends WarpTurn>> WARPS = List.of(
            TAmethystWarp.class, TCliffWarp.class, TDesertWarp.class, TEndWarp.class, TFrozenWarp.class,
            TMeadowWarp.class, TMushroomWarp.class, TNerdWarp.class, TNetherWarp.class, TOceanWarp.class,
            TRedstoneWarp.class, TSunWarp.class, TVoidWarp.class, TWoodWarp.class
    );

    public static void load() {
        getStructures().forEach(StructureManager::addStructure);

        for (Class<? extends WarpTurn> warp : WARPS) {
            try {
                addStructure(warp.getConstructor(World.class).newInstance(Bukkit.getWorld("world")).referenceStructure());
            } catch (Exception e) {
                NeoBB.LOG.warn("Couldn't load warp structure!", e);
            }
        }
    }

    public static void addStructure(Structure structure) {
        STRUCTURES.add(structure);
        Arrays.stream(structure.blocks).flatMap(Arrays::stream).flatMap(Arrays::stream)
                .forEach(b -> STRUCTURE_MATERIALS.add(b.material()));
    }

    public static boolean isStructureMaterial(Material material) {
        return STRUCTURE_MATERIALS.contains(material);
    }

    public static @Nullable Structure structure(String name) {
        for (Structure structure : STRUCTURES) {
            if (structure.name.equals(name))
                return structure;
        }
        return null;
    }

    public static List<Structure> getStructures() {
        String mode = Objects.requireNonNullElse(NeoBB.CONFIG.getString("structure-mode"), "local");
        try {
            if (mode.equals("server")) {
                HttpRequest request = HttpRequest.newBuilder(new URI("https://marcpg.com/neobb/structure/all")).GET().build();
                String response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
                return new Gson().fromJson(response, new TypeToken<List<Structure>>() {}.getType());
            } else {
                List<Structure> structures = new ArrayList<>();
                File[] structureFiles = NeoBB.STRUCTURE_DIR.toFile().listFiles();
                if (structureFiles == null || structureFiles.length == 0) return List.of();
                for (File structureFile : structureFiles) {
                    structures.add(new Gson().fromJson(new FileReader(structureFile), Structure.class));
                }
                return structures;
            }
        } catch (Exception e) {
            return List.of();
        }
    }

    public static @Nullable Structure match(Arena arena) {
        for (Structure structure : STRUCTURES) {
            if (structure.isInArena(arena))
                return structure;
        }
        return null;
    }
}
