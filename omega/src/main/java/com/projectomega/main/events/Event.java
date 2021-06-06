package com.projectomega.main.events;

/**
 * Represents a basic event
 */
public abstract class Event {

    private final boolean cancellable = getClass().isAnnotationPresent(Cancellable.class);

    private boolean cancelled;

    public boolean isCancelled() {
        return cancelled;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public void setCancelled(boolean cancelled) {
        if (cancellable)
            this.cancelled = cancelled;
        // could possibly log a warning that a listener attempted to cancel a non-cancellable event?
    }
}
