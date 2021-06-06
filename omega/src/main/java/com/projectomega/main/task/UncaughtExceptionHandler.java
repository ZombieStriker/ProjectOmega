package com.projectomega.main.task;

public interface UncaughtExceptionHandler {
    UncaughtExceptionHandler PRINT_TO_CONSOLE = (thread, task, error) -> error.printStackTrace();
    void onExceptionUncaught(TaskThread thread, Task task, Throwable error);
}
