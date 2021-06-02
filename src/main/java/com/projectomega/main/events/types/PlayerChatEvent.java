package com.projectomega.main.events.types;

import com.projectomega.main.events.Cancellable;
import com.projectomega.main.events.Event;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.TranslatedComponent;

@Cancellable
public class PlayerChatEvent extends Event {

    private String message;
    private TranslatedComponent.Builder json;
    private final Player player;

    public PlayerChatEvent(Player player, String message, TranslatedComponent.Builder json) {
        this.player = player;
        this.message = message;
        this.json = json;
    }

    public TranslatedComponent.Builder getJson() {
        return json;
    }

    public Player getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
