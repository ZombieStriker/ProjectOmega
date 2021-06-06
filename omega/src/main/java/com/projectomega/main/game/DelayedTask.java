package com.projectomega.main.game;

public abstract class DelayedTask {

    private boolean cancelled = false;

    public abstract void run();

    public void cancel(){
        this.cancelled = true;
    }
    public boolean isCancelled(){
        return cancelled;
    }
}
