package com.projectomega.main.game.chat;

import lombok.Getter;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

@Getter
public final class TextComponent extends TextMessage {

    public static final char COLOR_CHAR = '\u00A7';

    private final String text;
    private final @Nullable String insertion;
    private final Hover hoverEvent;
    private final Click clickEvent;

    private transient final String asJson;

    private TextComponent(String text, @Nullable String insertion, Hover hoverEvent, Click clickEvent) {
        this.text = text;
        this.insertion = insertion;
        this.hoverEvent = hoverEvent;
        this.clickEvent = clickEvent;
        asJson = GSON.toJson(this);
    }

    public Builder asBuilder() {
        return new Builder(this);
    }

    public static TextComponent simple(@NonNull String message) {
        return new TextComponent(colorize(message), null, null, null);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String message = "";
        private String insertion = null;
        private Hover hover = null;
        private Click click = null;

        public Builder() {
        }

        public Builder(TextComponent component) {
            message = component.text;
            insertion = component.insertion;
            hover = component.hoverEvent;
            click = component.clickEvent;
        }

        public Builder message(@NonNull String message) {
            this.message = colorize(message);
            return this;
        }

        public Builder hover(@NonNull Hover hover) {
            this.hover = hover;
            return this;
        }

        public Builder click(@NonNull Click click) {
            this.click = click;
            return this;
        }

        public Builder insertion(@NonNull String insertion) {
            this.insertion = insertion;
            return this;
        }

        public TextComponent build() {
            return new TextComponent(message, insertion, hover, click);
        }
    }

    public static String colorize(@NonNull String textToTranslate) {
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == '&' && "0123456789AaBbCcDdEeFfKkLlMmNnOoRrXx".indexOf(b[i + 1]) > -1) {
                b[i] = COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }
}
