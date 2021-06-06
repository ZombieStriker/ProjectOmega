package com.projectomega.main.task;

import java.util.concurrent.*;

/**
 * TaskManager manages task inside the server process including main server thread and asynchronous server thread
 */
public class TaskManager {

    private TaskThread main = new TaskThread(Executors.newSingleThreadScheduledExecutor());
    private TaskThread async = new TaskThread(Executors.newSingleThreadScheduledExecutor());

    public TaskThread getMainThread() {
        return main;
    }

    public TaskThread getAsynchronousThread() {
        return async;
    }

}
