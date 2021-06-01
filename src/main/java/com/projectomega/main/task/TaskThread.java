package com.projectomega.main.task;

import com.projectomega.main.plugin.*;
import com.projectomega.main.plugin.loader.*;

import java.util.*;
import java.util.concurrent.*;

public class TaskThread {
    private ScheduledExecutorService service;
    private Set<UncaughtExceptionHandler> exceptionHandlers = ConcurrentHashMap.newKeySet();
    private Set<Task> tasks = ConcurrentHashMap.newKeySet();

    public TaskThread(ScheduledExecutorService service) {
        this.service = service;
        registerUncaughtExceptionHandler(UncaughtExceptionHandler.PRINT_TO_CONSOLE);
    }

    protected void reportError(Task task, Throwable error) {
        exceptionHandlers.forEach(handler -> handler.onExceptionUncaught(this, task, error));
    }

    public void registerUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
        exceptionHandlers.add(handler);
    }

    public void unregisterUncaughtExceptionHandler(UncaughtExceptionHandler handler) {
        exceptionHandlers.remove(handler);
    }

    public void unregisterUncaughtExceptionHandler(OmegaPlugin plugin) {
        exceptionHandlers.removeIf(handler -> {
            ClassLoader loader = handler.getClass().getClassLoader();
            return loader instanceof PluginClassLoader;
            // TODO get the plugin associated with the PluginClassLoader and compare it with the "plugin" argument
        });
    }

    public void cancelTask(OmegaPlugin plugin) {
        tasks.removeIf(task -> {
            ClassLoader loader = task.getClass().getClassLoader();
            if (loader instanceof PluginClassLoader) {
                // TODO check if the associated plugin is the same plugin as specified in the parameter
                task.getFuture().cancel(false);
                return true;
            }
            return false;
        });
    }

    public void cancelTask(Task task) {
        if (task.getThread() == this) {
            task.getFuture().cancel(false);
            tasks.remove(task);
            task.unregister(this);
            task.setState(TaskState.CANCELLED);
        }
    }

    public void runTask(Task task) {
        task.register(this, service.submit(
                () -> {
                    task.impl_run();
                    tasks.remove(task);
                }
        ));
        tasks.add(task);
    }

    public void runTaskLater(Task task, Duration delay) {
        task.register(this, service.schedule(
                () -> {
                    task.impl_run();
                    tasks.remove(task);
                },
                delay.getDurationInMillis(),
                TimeUnit.MILLISECONDS));
        tasks.add(task);
    }

    public void runTaskRepeatedly(Task task, Duration delay, Duration interval) {
        if (task instanceof CallableTask) throw new UnsupportedOperationException();
        task.register(this, service.scheduleAtFixedRate(
                task::impl_run,
                delay.getDurationInMillis(),
                interval.getDurationInMillis(),
                TimeUnit.MILLISECONDS));
        tasks.add(task);
    }

    public void cancelAllTasks() {
        for (Task task : tasks) {
            task.getFuture().cancel(false);
            tasks.remove(task);
            task.unregister(this);
            task.setState(TaskState.CANCELLED);
        }
        tasks.clear();
    }
}
