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
import java.nio.file.StandardCopyOption

object StructureManager {
    val baseStructures = listOf(
        "default.daylight_sensor_line",
        "default.glass_wall.blue",
        "default.glass_wall.default",
        "default.glass_wall.green",
        "default.glass_wall.orange",
        "default.glass_wall.pink",
        "default.glass_wall.red",
        "default.glass_wall.white",
        "default.iron_bar_jail",
        "default.oak_door_turtling",
        "default.pumpkin_wall",
        "default.redstone_wall",
        "default.warp.amethyst",
        "default.warp.cliff",
        "default.warp.desert",
        "default.warp.end",
        "default.warp.frozen",
        "default.warp.meadow",
        "default.warp.mushroom",
        "default.warp.nerd",
        "default.warp.nether",
        "default.warp.ocean",
        "default.warp.redstone",
        "default.warp.sun",
        "default.warp.void",
        "default.warp.wood",
        "legacy.daylight_sensor_strip",
        "legacy.glass_wall.default",
        "legacy.glass_wall.light_blue",
        "legacy.glass_wall.lime",
        "legacy.nether_portal",
        "legacy.pumpkin_wall",
        "legacy.redstone_block_wall",
        "legacy.warp.nether",
        "legacy.warp.snow",
        "legacy.warp.sun",
        "legacy.warp.void",
        "legacy.warp.wood",
        "person.candle_circle",
        "person.pumpkin_wall",
        "person.stone_wall",
        "person.torch_circle",
        "person.turtling",
        "person.warp.amethyst",
        "person.warp.fire",
        "person.warp.ice",
        "person.warp.snow",
        "person.warp.villager",
        "person.warp.void",
        "person.wood_wall",
    )

    val LOADED: MutableList<Structure> = mutableListOf()
    val MATERIALS: MutableSet<Material> = mutableSetOf()

    fun saveFromResources(targetDir: Path) {
        val classLoader = this.javaClass.classLoader
        for (structure in baseStructures) {
            val name = "$structure.json"
            classLoader.getResourceAsStream("structures/$name").use { Files.copy(it ?: error("No resource structures/$name"), targetDir.resolve(name), StandardCopyOption.REPLACE_EXISTING) }
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
