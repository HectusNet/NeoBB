package net.hectus.neobb.structure;

import net.hectus.neobb.util.Cord;
import org.bukkit.Material;

import java.io.Serializable;

public record BlockInfo(Cord cord, Material material) implements Serializable {}
