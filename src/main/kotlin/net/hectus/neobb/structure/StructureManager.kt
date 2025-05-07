package net.hectus.neobb.structure

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.hectus.neobb.game.util.Arena
import net.hectus.neobb.util.Configuration
import org.bukkit.Material
import java.io.FileReader
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.Files
import java.nio.file.Path

object StructureManager {
    val baseStructures = listOf(
        "amethyst-warp",
        "blue_glass_wall",
        "cliff-warp",
        "daylight_sensor_line",
        "desert-warp",
        "end-warp",
        "frozen-warp",
        "glass_wall",
        "green_glass_wall",
        "iron_bar_jail",
        "legacy-candle-warp",
        "legacy-daylight_sensor_strip",
        "legacy-nether_portal",
        "legacy-nether-warp",
        "legacy-pumpkin_wall",
        "legacy-redstone_block_wall",
        "legacy-snow-warp",
        "legacy-sun-warp",
        "legacy-torch-warp",
        "legacy-void-warp",
        "legacy-wood-warp",
        "light_blue_glass_wall",
        "meadow-warp",
        "mushroom-warp",
        "nerd-warp",
        "nether-warp",
        "oak_door_turtling",
        "ocean-warp",
        "orange_glass_wall",
        "person-amethyst-warp",
        "person-fire-warp",
        "person-ice-warp",
        "person-snow-warp",
        "person-villager-warp",
        "person-void-warp",
        "pink_glass_wall",
        "pumpkin_wall",
        "red_glass_wall",
        "redstone_wall",
        "redstone-warp",
        "stone_wall",
        "sun-warp",
        "void-warp",
        "white_glass_wall",
        "wood_wall",
        "wood-warp",
    )

    val LOADED: MutableList<Structure> = mutableListOf()
    val MATERIALS: MutableSet<Material> = mutableSetOf()

    fun saveFromResources(targetDir: Path) {
        val classLoader = this.javaClass.classLoader
        for (structure in baseStructures) {
            val name = "$structure.json"
            classLoader.getResourceAsStream("structures/$name").use { Files.copy(it ?: error("No resource structures/$name"), targetDir.resolve(name)) }
        }
    }

    fun load() {
        getStructures().forEach { add(it) }
    }

    operator fun get(name: String): Structure? {
        return LOADED.find { it.name == name }
    }

    fun add(structure: Structure) {
        LOADED.add(structure)
        structure.blocks.forEach { block -> MATERIALS.add(block?.material ?: Material.AIR) }
        MATERIALS.remove(Material.AIR)
    }

    fun remove(structure: Structure) {
        LOADED.remove(structure)
        structure.remove()
    }

    private fun getStructures(): List<Structure> {
        if (Configuration.STRUCTURE_MODE == Configuration.StructureMode.SERVER) {
            val request = HttpRequest.newBuilder(URI("https://marcpg.com/neobb/structure/all")).GET().build()
            val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString()).body()
            return Gson().fromJson(response, object : TypeToken<List<Structure>>() {})
        } else {
            val structureDir = Configuration.STRUCTURE_MODE.pathSupplier.invoke().toFile()
            saveFromResources(structureDir.toPath())

            val structures = mutableListOf<Structure>()
            val structureFiles = structureDir.listFiles()
            if (structureFiles.isNullOrEmpty()) return emptyList()

            for (file in structureFiles) {
                structures.add(Gson().fromJson(FileReader(file), Structure::class.java))
            }
            return structures
        }
    }

    fun match(arena: Arena): Structure? {
        for (structure in LOADED) {
            if (structure.isInSpace(arena.placedSpace))
                return structure

            if (structure.blocks.x != structure.blocks.z) {
                val rotated = structure.rotated()
                if (rotated.isInSpace(arena.placedSpace))
                    return rotated
            }
        }
        return null
    }
}
