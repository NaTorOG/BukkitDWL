package net.pino.api;

import net.pino.BukkitDistributedWork;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletionStage;


public class LoadBuilder {

        private int maxTasksPerTick = 1;
        private int interval = 1;
        private int initialDelay = 10;
        private boolean stopWhenEmpty = false;
        private Runnable callback = null;
        private final Set<BukkitWorkload> withInitialJobs = new HashSet<>();
        private boolean isAsync;
        private Plugin plugin;

        private  boolean callbackIsAsync = false;

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
        public boolean shouldStopWhenEmpty() {
            return stopWhenEmpty;
        }
        public boolean isAsync() {
            return isAsync;
        }

        public boolean isCallbackAsync() {
            return callbackIsAsync;
        }

        public Runnable getCallback() {
            return callback;
        }

        public Set<BukkitWorkload> getWithInitialJobs() {
            return withInitialJobs;
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

        public LoadBuilder callback(Runnable callback) {
            return this.callback(callback, false);
        }

        public LoadBuilder callbackAsync(Runnable callback) {
            return this.callback(callback, true);
        }

        protected LoadBuilder callback(Runnable callback, boolean callbackIsAsync) {
            if(!stopWhenEmpty) return this;
            this.callback = callback;
            this.callbackIsAsync = callbackIsAsync;
            return this;
        }

        public LoadBuilder withInitialJobs(Set<BukkitWorkload> jobs) {
            this.withInitialJobs.addAll(jobs);
            return this;
        }

        public BukkitDistributedWork buildSync(Plugin plugin) {
            this.plugin = plugin;
            this.isAsync = false;
            return new BukkitDistributedWork(this);
        }

        public BukkitDistributedWork buildAsync(Plugin plugin) {
            this.plugin = plugin;
            this.isAsync = true;
            return new BukkitDistributedWork(this);
        }

    }

