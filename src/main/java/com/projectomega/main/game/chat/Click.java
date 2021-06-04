package com.projectomega.main.game.chat;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@ToString
@EqualsAndHashCode
public class Click {

    private final String value;
    private final String action;

    private Click(String value, String action) {
        this.value = value;
        this.action = action;
    }

    public String getValue() {
        return value;
    }

    public String getAction() {
        return action;
    }

    public static Click url(@NotNull String url) {
        return new Click(url, "open_url");
    }

    public static Click runCommand(@NotNull String command) {
        return new Click(command, "run_command");
    }

    public static Click suggest(@NotNull String text) {
        return new Click(text, "suggest_command");
    }

    public static Click of(@NotNull String action, @NotNull String value) {
        return new Click(value, action);
    }

}
