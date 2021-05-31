package com.projectomega.main.game;

import com.projectomega.main.game.chat.JsonChatBuilder;
import com.projectomega.main.game.chat.JsonChatElement;

import java.util.logging.Level;

public class ConsoleSender implements CommandSender {
    @Override
    public void sendMessage(String message) {
        Omega.getLogger().log(Level.INFO, message);
    }

    @Override
    public void issueCommand(String command) {
        //TODO: Issue command
    }

    @Override
    public void chat(String message) {
        JsonChatBuilder json = new JsonChatBuilder().setTranslate(JsonChatBuilder.CHAT_TYPE_TEXT).add(new JsonChatElement("Console")).add(new JsonChatElement(": " + message));
        Omega.broadcastJSONMessage(json.build());

    }
}
