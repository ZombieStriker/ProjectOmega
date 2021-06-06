package com.projectomega.main.game.chat;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.projectomega.main.game.chat.TextComponent.colorize;

@ToString
@EqualsAndHashCode
public final class Hover {

    private final String value;
    private final String action;

    private Hover(String value, String action) {
        this.value = value;
        this.action = action;
    }

    public String getValue() {
        return value;
    }

    public String getAction() {
        return action;
    }

    public static Hover tooltip(@NotNull String... lines) {
        return new Hover(colorize(String.join("\n", lines)), "show_text");
    }

    public static Hover tooltip(@NotNull List<String> lines) {
        return new Hover(colorize(String.join("\n", lines)), "show_text");
    }

    public static Hover of(@NotNull String action, @NotNull String text) {
        return new Hover(colorize(text), action);
    }

}
