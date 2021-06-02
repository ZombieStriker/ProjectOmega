package com.projectomega.main.task;

import com.projectomega.main.game.*;

import java.util.concurrent.*;

/**
 * A task that can run in one thread. A task cannot be run in multiple thread!
 */
public abstract class Task {
    private TaskState state = TaskState.IDLE;

    private Future<?> future;
    private TaskThread thread;

    // ABSTRACTION
    protected abstract void run();

    // INTERNAL API

    /**
     * Claim this task as the thread's registered task
     * @param thread
     * @param future
     */
    protected void register(TaskThread thread, Future<?> future) {
        if (this.thread != null) throw new IllegalStateException("task has already been running");
        this.thread = thread;
        this.future = future;
    }

    /**
     * Get the current registered TaskThread
     * @return
     */
    protected TaskThread getThread() {
        return thread;
    }

    /**
     * Unclaim this task from the specified thread
     * @param thread
     */
    protected void unregister(TaskThread thread) {
        if (this.thread == thread) {
            this.thread = null;
            this.future = null;
        }
    }

    /**
     * Force to set the state of this task
     * @param state
     */
    protected void setState(TaskState state) {
        this.state = state;
    }

    protected Future<?> getFuture() {
        return future;
    }

    protected void impl_run() {
        setState(TaskState.RUNNING);
        try {
            run();
        } catch (Throwable t) {
            thread.reportError(this, t);
            setState(TaskState.FAILED);
            return;
        }
        setState(TaskState.DONE);
    }

    /**
     * Get the current state of this task
     * @return the current state of this task
     */
    public TaskState getState() {
        return state;
    }

    // DELEGATED API

    /**
     * Cancel this task
     * @return true if the task is cancelled successfully
     */
    public boolean cancel() {
        if (thread != null) {
            thread.cancelTask(this);
        }
        return false;
    }

    /**
     * Run a task in main thread
     * @see TaskManager#getMainThread()
     * @see TaskThread#runTask(Task)
     */
    public void runTask() {
        Omega.getTaskManager().getMainThread().runTask(this);
    }

    /**
     * Run a delayed task in main thread
     * @see TaskManager#getMainThread()
     * @see TaskThread#runTaskLater(Task, Duration)
     * @param delay the delay before the execution
     */
    public void runTaskLater(Duration delay) {
        Omega.getTaskManager().getMainThread().runTaskLater(this, delay);
    }

    /**
     * Run task repeatedly in main thread
     * @see TaskManager#getMainThread()
     * @see TaskThread#runTaskRepeatedly(Task, Duration, Duration)
     * @param delay the delay before the execution
     * @param period the interval of the execution
     */
    public void runTaskRepeatedly(Duration delay, Duration period) {
        Omega.getTaskManager().getMainThread().runTaskRepeatedly(this, delay, period);
    }

    /**
     * Run a task in async thread
     * @see TaskManager#getAsynchronousThread()
     * @see TaskThread#runTask(Task)
     */
    public void runTaskAsynchronously() {
        Omega.getTaskManager().getAsynchronousThread().runTask(this);
    }

    /**
     * Run a delayed task in async thread
     * @see TaskManager#getAsynchronousThread()
     * @see TaskThread#runTaskLater(Task, Duration)
     * @param delay the delay before the execution
     */
    public void runTaskLaterAsynchronously(Duration delay) {
        Omega.getTaskManager().getAsynchronousThread().runTaskLater(this, delay);
    }

    /**
     * Run task repeatedly in async thread
     * @see TaskManager#getAsynchronousThread()
     * @see TaskThread#runTaskRepeatedly(Task, Duration, Duration)
     * @param delay the delay before the execution
     * @param period the interval of the execution
     */
    public void runTaskRepeatedlyAsynchronously(Duration delay, Duration period) {
        Omega.getTaskManager().getAsynchronousThread().runTaskRepeatedly(this, delay, period);
    }
}