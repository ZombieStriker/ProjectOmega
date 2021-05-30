package com.projectomega.main.game.events;

public class CancelableEvent extends Event{

    private boolean cancel = false;

    public boolean isCanceled(){
        return cancel;
    }
    public void setCancelled(boolean cancel){
        this.cancel = cancel;
    }
}
