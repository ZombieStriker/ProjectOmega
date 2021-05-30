package com.projectomega.main.game.events.types;

import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.JsonChatBuilder;
import com.projectomega.main.game.events.CancelableEvent;

public class PlayerChatEvent extends CancelableEvent {

    private String message;
    private Player player;
    private JsonChatBuilder json;

    public PlayerChatEvent(Player player, String message, JsonChatBuilder json){
        this.player = player;
        this.message = message;
        this.json = json;
    }

    public JsonChatBuilder getJson(){
        return json;
    }
    public Player getPlayer(){
        return player;
    }
    public String getMessage(){
        return message;
    }
    public void setMessage(String message){
        this.message = message;
    }
}
