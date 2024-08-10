package net.hectus.bb.structure;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class StructureCalculator {
    /**
     * Compares a specific structure to another, to get the similarity in P%. <br>
     * Will also compare to a rotated version of the reference. The returned value is the higher of the two.
     * @param query The structure to get the similarity of.
     * @param reference The reference structure that's 100% similar.
     * @return The similarity in P% from 0.0 to 1.0.
     */
    public static double compare(@NotNull Structure query, @NotNull Structure reference) {
        if (invalid(query, reference)) return 0.0;

        Structure rotatedReference = reference.rotated();

        int matchingBlocks = 0;
        int matchingBlocksRotated = 0;
        for (BlockData queryBlock : query.data()) {
            if (reference.data().contains(queryBlock))
                matchingBlocks++;
            if (rotatedReference.data().contains(queryBlock))
                matchingBlocksRotated++;
        }
        return (double) Math.max(matchingBlocks, matchingBlocksRotated) / reference.data().size();
    }

    /**
     * Predicts the top 5 most likely structures to be placed based on a query. <br>
     * Will also compare to rotated versions. The value in the final map is the higher of the two.
     * @param query The placed down structure to use for the prediction.
     * @return A LinkedHashMap of the top 5 most likely structures, in descending order, so the first is most likely and the last is the least likely.
     * @see #compare(Structure, Structure)
     */
    public static LinkedHashMap<Structure, Double> predict(Structure query) {
        Map<Structure, Double> predictions = new HashMap<>();

        for (Structure structure : StructureManager.loadedStructures()) {
            predictions.put(structure, compare(query, structure));
        }

        return predictions.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    private static boolean invalid(@NotNull Structure query, Structure reference) {
        for (Map.Entry<Material, Integer> material : query.materialWeights().entrySet()) {
            if (!reference.materialWeights().containsKey(material.getKey()) || reference.materialWeights().get(material.getKey()) > material.getValue()) {
                return true;
            }
        }
        return false;
    }
}
