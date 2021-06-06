package com.projectomega.main.game.chat;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
public class TranslatedComponent extends TextMessage {

    private final String translate;
    private final List<TextComponent> with;
    private final transient String asJson;

    private TranslatedComponent(String translate, List<TextComponent> with) {
        this.translate = translate;
        this.with = with;
        asJson = GSON.toJson(this);
    }

    public static TranslatedComponent of(@NonNull String translate, @NonNull TextComponent... components) {
        return new TranslatedComponent(translate, Arrays.asList(components));
    }

    public static TranslatedComponent of(@NonNull String translate, @NonNull List<TextComponent> components) {
        return new TranslatedComponent(translate, Collections.unmodifiableList(components));
    }

    public Builder asBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String translate;
        private final List<TextComponent> components = new ArrayList<>();

        public Builder() {
        }

        public Builder(TranslatedComponent component) {
            translate = component.translate;
            components.addAll(component.with);
        }

        public Builder translate(@NonNull String translate) {
            this.translate = translate;
            return this;
        }

        public Builder add(@NonNull TextComponent... components) {
            Collections.addAll(this.components, components);
            return this;
        }

        public TranslatedComponent build() {
            return new TranslatedComponent(translate, Collections.unmodifiableList(components));
        }

    }

}
