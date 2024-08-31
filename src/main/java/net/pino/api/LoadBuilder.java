package net.pino.api;

import net.pino.AsyncBukkitDistributedWork;
import net.pino.SyncBukkitDistributedWork;
import org.bukkit.plugin.Plugin;


public class LoadBuilder {

        private int maxTasksPerTick = 1;
        private int interval = 1;
        private int initialDelay = 10;
        private boolean stopWhenEmpty = false;
        private Plugin plugin;

        public Plugin getPlugin() {
            return plugin;
        }
        public int getMaxTasksPerTick() {
            return maxTasksPerTick;
        }
        public int getTickInterval() {
            return interval;
        }
        public int getInitialDelay() {
            return initialDelay;
        }
        public boolean isStopWhenEmpty() {
            return stopWhenEmpty;
        }

        public LoadBuilder maxTasksPerTick(int maxTasksPerTick) {
            this.maxTasksPerTick = maxTasksPerTick;
            return this;
        }

        public LoadBuilder interval(int interval) {
            this.interval = interval;
            return this;
        }

        public LoadBuilder initialDelay(int initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }

        public LoadBuilder stopWhenEmpty() {
            this.stopWhenEmpty = true;
            return this;
        }

        public SyncBukkitDistributedWork buildSync(Plugin plugin) {
            this.plugin = plugin;
            return new SyncBukkitDistributedWork(this);
        }

        public AsyncBukkitDistributedWork buildAsync(Plugin plugin) {
            this.plugin = plugin;
            return new AsyncBukkitDistributedWork(this);
        }

    }

