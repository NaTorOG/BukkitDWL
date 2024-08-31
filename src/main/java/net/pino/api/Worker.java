package net.pino.api;

import org.bukkit.scheduler.BukkitTask;

import java.util.Deque;

public interface Worker {

    Deque<BukkitWorkload> workloadQueue();

    LoadBuilder builder();

    BukkitTask workerTask();

    default void addJob(BukkitWorkload workload) {
        workloadQueue().add(workload);
    }

    default void executeTask() {
        builder().getPlugin().getLogger().info("Worker started");
        for (int i = 0; i < builder().getMaxTasksPerTick(); i++) {
            BukkitWorkload workload = workloadQueue().poll();
            if (workload == null) {
                if(builder().isStopWhenEmpty()) stop();
                break;
            }
            workload.compute();
        }
    }

    default void stop(){
        if(workerTask() != null || !workerTask().isCancelled()){
            workerTask().cancel();
        }
    }
}
