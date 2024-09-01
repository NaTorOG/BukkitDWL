# BukkitDWL (BukkitDistributedWorkLoad)
A simple library to distribute tasks(jobs) across multiple ticks.

 [![](https://jitpack.io/v/NaTorOG/BukkitDWL.svg)](https://jitpack.io/#NaTorOG/BukkitDWL)
#### ADD TO YOUR PROJECT
` Replace RELEASE with jitpack version above`

Maven
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.NaTorOG</groupId>
    <artifactId>BukkitDWL</artifactId>
    <version>RELEASE</version>
</dependency>
```
Gradle
```sh
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}
```
```sh
dependencies {
        implementation 'com.github.NaTorOG:BukkitDWL:RELEASE'
}
```
### CREATE YOUR FIRST DISTRIBUTED WORKLOAD
Creating a DistributedWorkLoad is easy and can be done in a few lines of code.  
You can create a Sync or Async BukkitDistributedWorkLoad, the difference is that the Sync DistributedWorkLoad will run on the main thread and the Async DistributedWorkLoad will run on a separate thread.
```java
import net.pino.BukkitDistributedWork;
import net.pino.api.LoadBuilder;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    public BukkitDistributedWork syncBukkitDistributedWork;
    public BukkitDistributedWork asyncBukkitDistributedWork;

    @Override
    public void onEnable() {
        syncBukkitDistributedWork = new LoadBuilder()
                .maxTasksPerTick(1) // How many tasks can be executed per tick
                .interval(1) // How many ticks between each task
                .initialDelay(5) // How many ticks before the first task
                .stopWhenEmpty() //OPTIONAL - Stop the scheduler when there are no tasks left
                .buildSync(this); // Requires your Plugin instance
        
        asyncBukkitDistributedWork = new LoadBuilder()
                .maxTasksPerTick(1)
                .interval(1)
                .initialDelay(5)
                .stopWhenEmpty()
                .buildAsync(this);
    }
}
```
### SUBMITTING A NEW TASK (JOB)
First we need a class implementing BukkitWorkload, this class will contain the code to run.    
```java
import net.pino.api.BukkitWorkload;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class JobToRun implements BukkitWorkload {

    private final Block blockToChange;

    public JobToRun(Block blockToChange) {
        this.blockToChange = blockToChange;
    }

    @Override
    public void compute() {
        // Things to run
        blockToChange.setType(Material.STONE);
    }
}
```
Now we can submit a new task to the DistributedWorkLoad, in this case I want to replace all blocks in a region with stone.  
Using a syncDistributedWorkLoad will allow the plugin in this case to distribute the job across multiple ticks without freezing the server.
```java
import net.pino.BukkitDistributedWork;
import net.pino.api.LoadBuilder;
import net.pino.region.Region;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {
    
    @Override
    public void onEnable() {
        // This work is called fireAndForgetWork because it will run until 
        // it's done and then will not be anymore available
        final BukkitDistributedWork fireAndForgetWork = new LoadBuilder()
                .maxTasksPerTick(1)
                .interval(1)
                .initialDelay(5)
                .stopWhenEmpty()
                .buildSync(this);
        final Region region = new Random16x16Region();
        region.getBlocks().forEach(block -> syncBukkitDistributedWork.addJob(new JobToRun(block)));
    }
}
```
### STOPPING A DISTRIBUTED WORKLOAD
To stop a DistributedWorkLoad you can use the stop() method, this will stop the scheduler, remove pending jobs and ignore new jobs.
```java
import net.pino.BukkitDistributedWork;
import net.pino.api.LoadBuilder;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    public BukkitDistributedWork syncBukkitDistributedWork;
    public BukkitDistributedWork asyncBukkitDistributedWork;

    @Override
    public void onEnable() {
        syncBukkitDistributedWork = new LoadBuilder()
                .maxTasksPerTick(1)
                .interval(1) 
                .initialDelay(5) 
                .stopWhenEmpty() 
                .buildSync(this); 

        syncBukkitDistributedWork.stop();
        
        asyncBukkitDistributedWork = new LoadBuilder()
                .maxTasksPerTick(1)
                .interval(1)
                .initialDelay(5)
                .stopWhenEmpty()
                .buildAsync(this);
        
        asyncBukkitDistributedWork.stop();
    }
}
```
### RESTART A DISTRIBUTED WORKLOAD
To restart a DistributedWorkLoad you can use the restart() method, this will stop the scheduler, remove pending jobs and restart the scheduler.
```java
import net.pino.BukkitDistributedWork;
import net.pino.api.LoadBuilder;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    public BukkitDistributedWork syncBukkitDistributedWork;
    public BukkitDistributedWork asyncBukkitDistributedWork;

    @Override
    public void onEnable() {
        syncBukkitDistributedWork = new LoadBuilder()
                .maxTasksPerTick(1)
                .interval(1) 
                .initialDelay(5) 
                .stopWhenEmpty() 
                .buildSync(this); 

        syncBukkitDistributedWork.restart();
        
        asyncBukkitDistributedWork = new LoadBuilder()
                .maxTasksPerTick(1)
                .interval(1)
                .initialDelay(5)
                .stopWhenEmpty()
                .buildAsync(this);
        
        asyncBukkitDistributedWork.restart();
    }
}
```