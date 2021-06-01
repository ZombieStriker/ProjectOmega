package com.projectomega.main.task;

import java.util.concurrent.*;

/**
 * CallableTask allows you to compute value in different thread and/or time.
 * @param <T> the resulting type
 */
public abstract class CallableTask<T> extends Task {
    private CompletableFuture<T> completableFuture = new CompletableFuture<>();

    @Override
    protected final void run() {
        completableFuture.complete(call());
    }

    public CompletableFuture<T> submit() {
        runTask();
        return completableFuture;
    }

    public CompletableFuture<T> submitLater(Duration delay) {
        runTaskLater(delay);
        return completableFuture;
    }

    public CompletableFuture<T> submitAsynchronously() {
        runTaskAsynchronously();
        return completableFuture;
    }

    public CompletableFuture<T> submitLaterAsynchronously(Duration delay) {
        runTaskLaterAsynchronously(delay);
        return completableFuture;
    }

    public abstract T call();

}
