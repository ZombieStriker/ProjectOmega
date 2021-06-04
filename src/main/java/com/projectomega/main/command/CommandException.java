package com.projectomega.main.command;

import static com.projectomega.main.game.chat.TextComponent.colorize;

public class CommandException extends Exception {

    public CommandException(String message) {
        super(colorize(message));
    }
}
