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
        System.out.println("Queue size " + workloadQueue().size());
        for (int i = 0; i < builder().getMaxTasksPerTick(); i++) {
            System.out.println("Queue size before poll: " + workloadQueue().size());
            BukkitWorkload workload = workloadQueue().poll();
            System.out.println("Queue size after poll: " + workloadQueue().size());
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
