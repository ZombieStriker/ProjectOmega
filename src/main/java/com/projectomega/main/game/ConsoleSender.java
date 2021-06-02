package com.projectomega.main.game;

import com.projectomega.main.game.chat.TextMessage;

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
        Omega.broadcastJSONMessage(TextMessage.chat("Console", ": " + message).getAsJson());
    }
}
