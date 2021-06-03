package com.projectomega.main.task;

import com.projectomega.main.plugin.OmegaPlugin;
import com.projectomega.main.plugin.loader.PluginClassLoader;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
            return loader instanceof PluginClassLoader && ((PluginClassLoader) loader).getPlugin() == plugin;
        });
    }

    public void cancelTask(OmegaPlugin plugin) {
        tasks.removeIf(task -> {
            ClassLoader loader = task.getClass().getClassLoader();
            if (loader instanceof PluginClassLoader && ((PluginClassLoader) loader).getPlugin() == plugin) {
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
                    task.unregister(this);
                }
        ));
        tasks.add(task);
    }

    public void runTask(Runnable task) {
        runTask(Task.wrap(task));
    }

    public void runTaskLater(Task task, Duration delay) {
        task.register(this, service.schedule(
                () -> {
                    task.impl_run();
                    tasks.remove(task);
                    task.unregister(this);
                },
                delay.getDurationInMillis(),
                TimeUnit.MILLISECONDS));
        tasks.add(task);
    }

    public void runTaskLater(Runnable task, Duration delay) {
        runTaskLater(Task.wrap(task), delay);
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

    public void runTaskRepeatedly(Runnable task, Duration delay, Duration interval) {
        runTaskRepeatedly(Task.wrap(task), delay, interval);
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
