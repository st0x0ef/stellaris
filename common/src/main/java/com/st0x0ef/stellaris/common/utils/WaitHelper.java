package com.st0x0ef.stellaris.common.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WaitHelper {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public static void wait(int milliseconds, Runnable task) {
        scheduler.schedule(task, milliseconds, TimeUnit.MILLISECONDS);
    }

    public static void shutdown() {
        scheduler.shutdown();
    }
}
