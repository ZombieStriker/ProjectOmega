package com.projectomega.main.task;

/**
 * Describes the current state of a Task
 */
public enum TaskState {
    /**
     * The task is currently doing nothing. This does not determine whether the task is registered to a TaskThread.
     * A delayed task may have this state in the beginning even the task is registered to a TaskThread.
     */
    IDLE,
    /**
     * The task is currently running
     */
    RUNNING,
    /**
     * The task is failed to run completely. This state comes after {@link TaskState#RUNNING}.
     */
    FAILED,
    /**
     * The task is cancelled externally
     */
    CANCELLED,
    /**
     * The task is done successfully without any error. This state comes after {@link TaskState#RUNNING}.
     */
    DONE
}
