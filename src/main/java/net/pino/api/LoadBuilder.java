package net.pino.api;

import net.pino.AsyncBukkitDistributedWork;
import net.pino.SyncBukkitDistributedWork;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class LoadBuilder {

        private int maxTasksPerTick = 1;
        private int interval = 1;
        private boolean stopWhenEmpty = false;
        private Plugin plugin;

        public @NotNull Plugin getPlugin() {
            return plugin;
        }
        public int getMaxTasksPerTick() {
            return maxTasksPerTick;
        }
        public int getTickInterval() {
            return interval;
        }
        public boolean isStopWhenEmpty() { return stopWhenEmpty; }

        public LoadBuilder maxTasksPerTick(int maxTasksPerTick) {
            this.maxTasksPerTick = maxTasksPerTick;
            return this;
        }

        public LoadBuilder interval(int interval) {
            this.interval = interval;
            return this;
        }

        public LoadBuilder stopWhenEmpty() {
            this.stopWhenEmpty = true;
            return this;
        }

        public SyncBukkitDistributedWork buildSync(@NotNull Plugin plugin) {
            this.plugin = plugin;
            return new SyncBukkitDistributedWork(this);
        }

        public AsyncBukkitDistributedWork buildAsync(@NotNull Plugin plugin) {
            this.plugin = plugin;
            return new AsyncBukkitDistributedWork(this);
        }

    }

