package net.hectus.neobb.game.util

class TurnScheduler {
    private val tasks: MutableList<Task> = mutableListOf()

    fun tick() {
        for (task in tasks.toList()) {
            if (--task.countdown <= 0) {
                task.action.invoke()
                tasks.remove(task)
            }
        }
    }

    fun remove(id: ScheduleID) {
        tasks.removeAll { it.id == id }
    }

    fun exists(id: ScheduleID): Boolean {
        return tasks.any { it.id == id }
    }

    fun runTaskLater(id: ScheduleID, turns: Int, action: () -> Unit) {
        tasks.add(Task(id, action, turns))
    }

    fun runTaskTimer(id: ScheduleID, interval: Int, predicate: () -> Boolean = { true }, action: () -> Unit) {
        tasks.add(Task(id, {
            action.invoke()
            if (predicate.invoke())
                runTaskTimer(id, interval, predicate, action)
        }, interval))
    }

    /**
     * Represents a simple task that can be used in the scheduler.
     * @param id A unique string for this task. Needed for cancelling events.
     * @param action The runnable to run once the task is executed.
     * @param countdown The countdown in turns until the task gets executed.
     */
    data class Task(val id: ScheduleID, val action: () -> Unit, var countdown: Int)
}

enum class ScheduleID {
    BLINDNESS,
    BURN,
    EXTRA_TURN,
    FREEZE,
    GLASS_WALL_DEFENSE,
    ICE,
    IRON_BAR_JAIL,
    LEVITATION,
    OAK_DOOR_TURTLING,
    POISON,
    REDSTONE_BLOCK_WALL,
    REDSTONE_POWER,
    REVIVE,
    SNOW_GOLEM,
}
