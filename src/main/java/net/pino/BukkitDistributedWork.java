package net.pino;

import net.pino.api.BukkitWorkload;
import net.pino.api.LoadBuilder;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;

public class BukkitDistributedWork {

    private final Deque<BukkitWorkload> workloadQueue = new ArrayDeque<>();
    private final LoadBuilder builder;
    private boolean isActive = false;
    private BukkitTask workerTask;

    public BukkitDistributedWork(LoadBuilder builder) {
        this.builder = builder;
        start();
    }

    private void start(){
        if(!builder().getWithInitialJobs().isEmpty()){
            workloadQueue.addAll(builder().getWithInitialJobs());
        }

        setActive(true);
        this.workerTask = builder.isAsync()
                ? Bukkit.getScheduler().runTaskTimerAsynchronously(
                builder.getPlugin(),
                this::executeTask,
                builder().getInitialDelay(),
                builder.getTickInterval())

                : Bukkit.getScheduler().runTaskTimer(
                        builder.getPlugin(),
                        this::executeTask,
                        builder().getInitialDelay(),
                        builder.getTickInterval());
    }

    private void executeTask() {
        if(!isActive()) return;

        if(workloadQueue.isEmpty()) {
            isActive = false;
            if(builder().shouldStopWhenEmpty()){
                if(builder.getCallback() == null){
                    stop();
                    return;
                }

                if(builder.isCallbackAsync()) {
                    Bukkit.getScheduler().runTaskAsynchronously(builder.getPlugin(), () -> {
                        builder.getCallback().run();
                        Bukkit.getScheduler().runTask(builder.getPlugin(), this::stop);
                    });

                    return;
                }

                Bukkit.getScheduler().getMainThreadExecutor(builder.getPlugin()).execute(() -> {
                    builder.getCallback().run();
                    stop();
                });
            }
            return;
        }

        for (int i = 0; i < builder().getMaxTasksPerTick(); i++) {
            BukkitWorkload workload = workloadQueue.poll();
            if (workload == null) {
                break;
            }
            try {
                workload.compute();
            } catch (Exception exception) {
                builder.getPlugin().getLogger()
                        .log(Level.SEVERE, 
                        "An error occurred while executing a workload, aborting...",
                        exception);
                stop();
            }
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
