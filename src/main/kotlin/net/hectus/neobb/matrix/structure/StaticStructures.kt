package net.hectus.neobb.matrix.structure

enum class StaticStructures : StaticStructure {
    ;

    override val key: String = name.lowercase()

    enum class Default : StaticStructure {
        DAYLIGHT_SENSOR_LINE,
        IRON_BAR_JAIL,
        OAK_DOOR_TURTLING,
        PUMPKIN_WALL,
        REDSTONE_WALL;

        override val key: String = "default.${name.lowercase()}"

        enum class GlassWall : StaticStructure {
            BLUE,
            DEFAULT,
            GREEN,
            ORANGE,
            PINK,
            RED,
            WHITE;

            override val key: String = "default.glass_wall.${name.lowercase()}"
        }

        enum class Warp : StaticStructure {
            AMETHYST,
            CLIFF,
            DESERT,
            END,
            FROZEN,
            MEADOW,
            MUSHROOM,
            NERD,
            NETHER,
            OCEAN,
            REDSTONE,
            SUN,
            VOID,
            WOOD;

            override val key: String = "default.warp.${name.lowercase()}"
        }
    }

    enum class Legacy : StaticStructure {
        DAYLIGHT_SENSOR_STRIP,
        NETHER_PORTAL,
        PUMPKIN_WALL,
        REDSTONE_BLOCK_WALL;

        override val key: String = "legacy.${name.lowercase()}"

        enum class GlassWall : StaticStructure {
            DEFAULT,
            LIGHT_BLUE,
            LIME;

            override val key: String = "default.glass_wall.${name.lowercase()}"
        }

        enum class Warp : StaticStructure {
            NETHER,
            SNOW,
            SUN,
            VOID,
            AETHER, //
            AMETHYST,
            BOOK, //
            CLIFF, //
            DESERT, //
            END, //
            HEAVEN, //
            HELL, //
            ICE, //
            MUSHROOM,
            REDSTONE, //
            UNDERWATER, //
            WOOD;

            override val key: String = "legacy.warp.${name.lowercase()}"
        }
    }

    enum class Person : StaticStructure {
        CANDLE_CIRCLE,
        PUMPKIN_WALL,
        STONE_WALL,
        TORCH_CIRCLE,
        TURTLING,
        WOOD_WALL;

        override val key: String = "person.${name.lowercase()}"

        enum class Warp : StaticStructure {
            AMETHYST,
            FIRE,
            ICE,
            SNOW,
            VILLAGER,
            VOID;

            override val key: String = "person.warp.${name.lowercase()}"
        }
    }
}

sealed interface StaticStructure {
    val key: String
    val structure: Structure
        get() = StructureManager[key]!!
}
