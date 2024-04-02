package net.hectus.bb.structure;

import net.hectus.bb.BlockBattles;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class StructureManager {
    public static final File STRUCTURE_DIR = BlockBattles.DATA_DIR.resolve("structures").toFile();
    private static final List<Structure> STRUCTURES = new ArrayList<>();

    public static void save() {
        for (Structure structure : STRUCTURES) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(STRUCTURE_DIR, structure.name())))) {
                out.writeObject(structure);
            } catch (IOException e) {
                BlockBattles.LOG.error("There was an issue while saving the structure \"" + structure.name() + "\": " + e.getMessage());
            }
        }
    }

    public static void load() {
        STRUCTURES.clear();
        for (File file : Objects.requireNonNull(STRUCTURE_DIR.listFiles())) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                STRUCTURES.add((Structure) in.readObject());
            } catch (IOException | ClassNotFoundException e) {
                BlockBattles.LOG.error("There was an issue while loading the structure \"" + file.getName() + "\": " + e.getMessage());
            }
        }
    }

    public static @Nullable Structure get(String name) {
        for (Structure structure : STRUCTURES) {
            if (structure.name().equals(name))
                return structure;
        }
        return null;
    }

    public static boolean remove(String name) {
        return STRUCTURES.removeIf(s -> s.name().equals(name));
    }
}
