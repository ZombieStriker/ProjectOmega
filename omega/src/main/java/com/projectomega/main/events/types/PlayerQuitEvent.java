package com.projectomega.main.events.types;

import com.projectomega.main.events.Event;
import com.projectomega.main.game.Player;

public class PlayerQuitEvent extends Event {

    private Player player;

    public PlayerQuitEvent(Player player){
        this.player =player;
    }

    public Player getPlayer() {
        return player;
    }
}
