package com.projectomega.main.events.types;

import com.projectomega.main.events.Cancellable;
import com.projectomega.main.events.Event;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.chat.JsonChatBuilder;

@Cancellable
public class PlayerChatEvent extends Event {

    private String message;
    private final Player player;
    private final JsonChatBuilder json;

    public PlayerChatEvent(Player player, String message, JsonChatBuilder json) {
        this.player = player;
        this.message = message;
        this.json = json;
    }

    public JsonChatBuilder getJson() {
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
