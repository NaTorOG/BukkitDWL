package net.pino.api;

import java.util.UUID;

public interface BukkitWorkload {

    UUID taskUUID = UUID.randomUUID();

    void compute();
}
