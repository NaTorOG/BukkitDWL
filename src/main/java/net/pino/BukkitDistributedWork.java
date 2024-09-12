package net.pino;

import net.pino.api.BukkitWorkload;
import net.pino.api.LoadBuilder;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayDeque;
import java.util.Deque;

public class BukkitDistributedWork {

    private final Deque<BukkitWorkload> workloadQueue = new ArrayDeque<>();
    private final LoadBuilder builder;
    private boolean isActive = false;
    private BukkitTask workerTask;
    private boolean callbackIsAsync = false;

    public BukkitDistributedWork(LoadBuilder builder) {
        this.builder = builder;
        start();
    }

    private void start(){
        if(!builder().getWithInitialJobs().isEmpty()){
            workloadQueue().addAll(builder().getWithInitialJobs());
        }

        if(builder().isAsync()){
            this.workerTask = Bukkit.getScheduler().runTaskTimerAsynchronously(
                    builder.getPlugin(),
                    this::executeTask,
                    builder().getInitialDelay(),
                    builder.getTickInterval());
        }else{
            this.workerTask = Bukkit.getScheduler().runTaskTimer(
                    builder.getPlugin(),
                    this::executeTask,
                    builder().getInitialDelay(),
                    builder.getTickInterval());
        }
        setActive(true);
    }

    private void executeTask() {
        for (int i = 0; i < builder().getMaxTasksPerTick(); i++) {
            BukkitWorkload workload = workloadQueue().poll();
            if (workload == null) {
                if(builder().shouldStopWhenEmpty()){
                    if(builder.getAsyncCallback() != null){
                        builder.getAsyncCallback().thenAcceptAsync((result) -> {
                            if(result) stop();
                        }, Bukkit.getScheduler().getMainThreadExecutor(builder.getPlugin()));

                    } else if (builder.getSyncCallback() != null) {
                        builder.getSyncCallback().run();
                        stop();
                        
                    }else{
                        stop();
                    }
                }
                break;
            }
            workload.compute();
        }
    }

    public void stop(){
        if(workerTask() != null || !workerTask().isCancelled()){
            workerTask().cancel();
        }
        workloadQueue().clear();
        setActive(false);
    }

    public void restart(){
        stop();
        this.workerTask = Bukkit.getScheduler().runTaskTimer(
                builder.getPlugin(),
                this::executeTask,
                builder().getInitialDelay(),
                builder.getTickInterval());
        setActive(true);
    }

    public void addJob(BukkitWorkload workload) {
        if(!isActive()) return;
        workloadQueue().add(workload);
    }

    public Deque<BukkitWorkload> workloadQueue() {
        return workloadQueue;
    }

    public LoadBuilder builder() {
        return builder;
    }

    public BukkitTask workerTask() {
        return workerTask;
    }

    public boolean isActive() {
        return this.isActive;
    }


    private void setActive(boolean active){
        this.isActive = active;
    }

}
