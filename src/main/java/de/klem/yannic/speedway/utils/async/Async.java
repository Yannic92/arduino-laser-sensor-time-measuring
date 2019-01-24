package de.klem.yannic.speedway.utils.async;

import de.klem.yannic.speedway.Speedway;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Async {

    private static final ExecutorService executor = Executors.newCachedThreadPool();
    ;

    public static Future<?> execute(Runnable runnable) {

        if (executor.isShutdown()) {
            throw new ExecutorShutdownException();
        }

        return executor.submit(runnable);
    }

    public static <T> Future<T> execute(Callable<T> runnable) {
        if (executor.isShutdown()) {
            throw new ExecutorShutdownException();
        }

        return executor.submit(runnable);
    }

    public static void shutdown() {
        executor.shutdown();
    }
}
