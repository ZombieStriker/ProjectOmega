package com.projectomega.main.plugin;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * Represents information about a plugin
 */
@Getter
public final class PluginMeta {

    private final String name;
    private final String mainClass;
    private final @Nullable String author;
    private final @Nullable String description;
    private final @Nullable String version;

    public PluginMeta(String name, String mainClass) {
        this(name, mainClass, "Built-in", null, "v1.0.0");
    }

    public PluginMeta(String name, String mainClass, @Nullable String author, @Nullable String description, @Nullable String version) {
        this.name = name;
        this.mainClass = mainClass;
        this.author = author;
        this.description = description;
        this.version = version;
    }

    public static PluginMeta read(@NotNull InputStream stream) {
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(stream);
        return read(data);
    }

    public static PluginMeta read(@NotNull Map<String, Object> data) {
        return new PluginMeta(
                requireNonNull(data.get("name"), "Missing 'name' property!").toString(),
                requireNonNull(data.get("main"), "Missing 'main' property!").toString(),
                data.get("author").toString(),
                data.getOrDefault("version", "1.0").toString(),
                data.get("description").toString()
        );
    }
}
