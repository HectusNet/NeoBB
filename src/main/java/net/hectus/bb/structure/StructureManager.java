package net.hectus.bb.structure;

import net.hectus.bb.BlockBattles;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StructureManager {
    public static File STRUCTURE_DIR = BlockBattles.globalStructures ? new File("~/.config/neobb-structures") : BlockBattles.DATA_DIR.resolve("structures").toFile();
    private static final List<Structure> LOADED_STRUCTURES = new ArrayList<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void save() {
        STRUCTURE_DIR.delete();
        STRUCTURE_DIR.mkdirs();

        for (Structure structure : LOADED_STRUCTURES) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(STRUCTURE_DIR, structure.name())))) {
                out.writeObject(structure);
            } catch (IOException e) {
                BlockBattles.LOG.error("There was an issue while saving the structure \"{}\": {}", structure.name(), e.getMessage());
            }
        }
    }

    public static void load() {
        LOADED_STRUCTURES.clear();
        for (File file : Objects.requireNonNull(STRUCTURE_DIR.listFiles())) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                LOADED_STRUCTURES.add((Structure) in.readObject());
            } catch (IOException | ClassNotFoundException e) {
                BlockBattles.LOG.error("There was an issue while loading the structure \"{}\": {}", file.getName(), e.getMessage());
            }
        }
    }

    public static @Nullable Structure get(String name) {
        for (Structure structure : LOADED_STRUCTURES) {
            if (structure.name().equals(name))
                return structure;
        }
        return null;
    }

    public static void add(Structure structure) {
        LOADED_STRUCTURES.add(structure);
    }

    public static boolean remove(String name) {
        return LOADED_STRUCTURES.removeIf(s -> s.name().equals(name));
    }

    public static List<String> names() {
        return LOADED_STRUCTURES.stream().map(Structure::name).toList();
    }
}
