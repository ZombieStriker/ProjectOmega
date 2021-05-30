package com.projectomega.main.events.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.events.CancelableEvent;

public class PlayerSendCommandEvent extends CancelableEvent {

    private Player player;
    private String command;

    public PlayerSendCommandEvent(Player player, String command){
        this.player = player;
        this.command = command;
    }
    public String getFullCommand(){
        return command;
    }
    public Player getPlayer(){
        return player;
    }
}
