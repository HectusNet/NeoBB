package net.hectus.bb.game.util;

import net.hectus.bb.event.custom.TurnDoneEvent;
import net.hectus.bb.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Static utility class that acts as a simple scheduler, similar to Bukkit's {@link Bukkit#getScheduler() scheduler},
 * but using turns as a "time" unit instead of ticks or seconds.
 */
public final class TurnScheduler implements Listener {
    private static final HashMap<Game, ArrayList<Task>> TASKS = new HashMap<>();

    @EventHandler
    public void onTurnDone(@NotNull TurnDoneEvent event) {
        if (TASKS.containsKey(event.game())) {
            for (Task task : TASKS.get(event.game())) {
                if (task.countdown.decrementAndGet() <= 0) {
                    task.runnable.run();
                    TASKS.get(event.game()).remove(task);
                }
            }
        }
    }

    /**
     * Schedules a task for later execution.
     * @param game What game's turns the task gets scheduled on.
     * @param uniqueId A unique string for this task. Needed for cancelling events.
     * @param turns The amount of turns until the task gets executed.
     * @param task The {@link Runnable Java-Runnable} to execute.
     * @see #runTaskLater(Game, Task)
     */
    public static void runTaskLater(Game game, String uniqueId, int turns, Runnable task) {
        runTaskLater(game, new Task(uniqueId, task, new AtomicInteger(turns)));
    }

    /**
     * Schedules a task for later execution.
     * @param game What game's turns the task gets scheduled on.
     * @param task The {@link Task custom task} with all important values to execute.
     * @see #runTaskLater(Game, String, int, Runnable)
     */
    public static void runTaskLater(Game game, Task task) {
        if (!TASKS.containsKey(game)) TASKS.put(game, new ArrayList<>());
        TASKS.get(game).add(task);
    }

    /**
     * Cancels a specific task based on the unique string. If there are multiple tasks
     * with the specified unique string, it will remove all of them.
     * @param game What game's turns the task(s) is/are scheduled on.
     * @param uniqueId The unique string to remove tasks based on.
     */
    public static void cancel(Game game, String uniqueId) {
        TASKS.get(game).removeIf(t -> uniqueId.equals(t.uniqueId));
    }

    /**
     * Cancels all tasks scheduled on a specific game, without caring about any other info.
     * Also the only proper way of fully removing the game map entry from the memory.
     * @param game What game's turns the task(s) is/are scheduled on.
     */
    public static void cancelAll(Game game) {
        TASKS.remove(game);
    }

    /**
     * Represents a simply task that can be used in the scheduler.
     * @param uniqueId A unique string for this task. Needed for cancelling events.
     * @param runnable The runnable to run once the task is executed.
     * @param countdown The countdown in turns until the task gets executed.
     */
    public record Task(String uniqueId, Runnable runnable, AtomicInteger countdown) {}
}
