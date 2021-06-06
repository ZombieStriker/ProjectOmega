package com.projectomega.main.game.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import lombok.NonNull;

import java.util.Arrays;
import java.util.stream.Collectors;

public abstract class TextMessage {

    protected static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    public abstract String getAsJson();

    public static TranslatedComponent translate(@NonNull String translate, @NonNull TextComponent... components) {
        return TranslatedComponent.of(translate, components);
    }

    public static TranslatedComponent translate(@NonNull String translate, @NonNull String... text) {
        return TranslatedComponent.of(translate, Arrays.stream(text).map(TextComponent::simple).collect(Collectors.toList()));
    }

    public static TranslatedComponent chat(@NonNull String... text) {
        return translate("chat.type.text", text);
    }

    public static TranslatedComponent chat(@NonNull TextComponent... text) {
        return translate("chat.type.text", text);
    }

    public static String text(@NonNull String text) {
        JsonObject o = new JsonObject();
        o.addProperty("text", text);
        return o.toString();
    }

    public static String text(@NonNull TextComponent text) {
        JsonObject o = new JsonObject();
        o.addProperty("text", text.getAsJson());
        return o.toString();
    }

    public static String wrap(@NonNull TextComponent... components) {
        return Arrays.toString(components);
    }

}
