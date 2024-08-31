package net.pino;

import net.pino.api.BukkitWorkload;
import net.pino.api.LoadBuilder;
import net.pino.api.Worker;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayDeque;
import java.util.Deque;

public class SyncBukkitDistributedWork implements Worker {

    private final Deque<BukkitWorkload> workloadQueue = new ArrayDeque<>();
    private final LoadBuilder builder;
    private final BukkitTask workerTask;

    public SyncBukkitDistributedWork(LoadBuilder builder) {
        this.builder = builder;
        this.workerTask = Bukkit.getScheduler().runTaskTimer(
                builder.getPlugin(),
                this::executeTask,
                10,
                builder.getTickInterval());
    }

    @Override
    public Deque<BukkitWorkload> workloadQueue() {
        return workloadQueue;
    }

    @Override
    public LoadBuilder builder() {
        return builder;
    }

    @Override
    public BukkitTask workerTask() {
        return workerTask;
    }

}
