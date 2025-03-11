package net.hectus.neobb.structure;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.hectus.neobb.NeoBB;
import net.hectus.neobb.game.util.Arena;
import net.hectus.neobb.util.Configuration;
import org.bukkit.Material;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public final class StructureManager {
    private static final List<Structure> STRUCTURES = new ArrayList<>();
    private static final Set<Material> STRUCTURE_MATERIALS = new HashSet<>();

    public static void load() {
        getStructures().forEach(StructureManager::add);
    }

    public static void add(Structure structure) {
        STRUCTURES.add(structure);
        Arrays.stream(structure.blocks).flatMap(Arrays::stream).flatMap(Arrays::stream)
                .forEach(b -> STRUCTURE_MATERIALS.add(b != null ? b.material() : Material.AIR));
    }

    public static void remove(Structure structure) {
        STRUCTURES.remove(structure);
        try {
            structure.remove();
        } catch (IOException e) {
            NeoBB.LOG.warn("Could not remove structure from storage.", e);
        }
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
        try {
            if (Configuration.STRUCTURE_MODE == Configuration.StructureMode.SERVER) {
                HttpRequest request = HttpRequest.newBuilder(new URI("https://marcpg.com/neobb/structure/all")).GET().build();
                String response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body();
                return new Gson().fromJson(response, new TypeToken<List<Structure>>() {}.getType());
            } else {
                List<Structure> structures = new ArrayList<>();
                File[] structureFiles = Configuration.STRUCTURE_MODE.path().toFile().listFiles();
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

            if (structure.blocksX != structure.blocksZ) {
                Structure rotated = structure.rotated();
                if (rotated.isInArena(arena))
                    return rotated;
            }
        }
        return null;
    }
}
