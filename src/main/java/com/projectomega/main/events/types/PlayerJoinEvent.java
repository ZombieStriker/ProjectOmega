package com.projectomega.main.events.types;

import com.projectomega.main.events.CancelableEvent;
import com.projectomega.main.game.Player;

public class PlayerJoinEvent extends CancelableEvent {

    private Player player;

    public PlayerJoinEvent(Player player){
        this.player = player;
    }

    public Player getPlayer() {
    return player;
    }
}
