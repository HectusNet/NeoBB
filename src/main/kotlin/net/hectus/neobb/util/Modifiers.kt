package net.hectus.neobb.util

enum class Modifiers {;

    enum class Player {
        EXTRA_TURN,
        NO_MOVE,
        NO_JUMP,
        REVIVE;

        enum class Default {
            ATTACKED,
            DEFENDED,
            ALWAYS_WARP,
            BOAT_DAMAGE,
            NO_ATTACK
        }
    }

    enum class Game {
        REDSTONE_POWER,
        NO_WARP;

        enum class Legacy {
            PORTAL_AWAIT,
            NO_ATTACK
        }

        enum class Person {
            FIRE_ENTITIES,
            BLUE_ENTITIES,
            WHITE_ENTITIES,
            VILLAGER_ENTITIES,
            NO_WIN_CONS,
            SNOW_GOLEM
        }
    }
}
