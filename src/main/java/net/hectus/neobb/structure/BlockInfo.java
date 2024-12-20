package net.hectus.neobb.structure;

import com.marcpg.libpg.storing.Cord;
import org.bukkit.Material;

import java.io.Serializable;

public record BlockInfo(Cord cord, Material material) implements Serializable {}
