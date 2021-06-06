package com.projectomega.main.game;

import com.projectomega.main.command.permission.PermissionHolder;

public interface CommandSender extends PermissionHolder {

    void sendMessage(String message);

    void issueCommand(String command);

    void chat(String message);

    default boolean isPlayer() {
        return this instanceof Player;
    }

    default boolean isConsole() {
        return this instanceof ConsoleSender;
    }

}
