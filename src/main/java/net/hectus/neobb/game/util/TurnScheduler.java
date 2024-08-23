package net.hectus.neobb.game.util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TurnScheduler {
    private final List<Task> tasks = new ArrayList<>();

    public void tick() {
        for (Task task : tasks) {
            if (task.countdown.decrementAndGet() <= 0) {
                task.runnable.run();
                tasks.remove(task);
            }
        }
    }

    public void removeTask(String id) {
        tasks.removeIf(t -> t.uniqueId.equals(id));
    }

    public boolean hasTask(String id) {
        return tasks.stream().anyMatch(t -> t.uniqueId.equals(id));
    }

    public void runTaskLater(String uniqueId, Runnable runnable, int turns) {
        tasks.add(new Task(uniqueId, runnable, new AtomicInteger(turns)));
    }

    /**
     * Represents a simply task that can be used in the scheduler.
     * @param uniqueId A unique string for this task. Needed for cancelling events.
     * @param runnable The runnable to run once the task is executed.
     * @param countdown The countdown in turns until the task gets executed.
     */
    record Task(String uniqueId, Runnable runnable, AtomicInteger countdown) {}
}
