package com.projectomega.main.game;

public interface CommandSender {

    void sendMessage(String message);

    void issueCommand(String command);

    void chat(String message);
}
