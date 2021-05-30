package com.projectomega.main.events;

/**
 * Represents numbered priority
 */
public enum Priority {

    LOWEST(5),
    LOW(4),
    NORMAL(3),
    HIGH(2),
    HIGHEST(1);

    private final int priority;

    Priority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
