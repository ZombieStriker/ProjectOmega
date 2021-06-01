package com.projectomega.main.plugin;

import com.projectomega.main.plugin.loader.dependency.Dependency;
import com.projectomega.main.plugin.loader.dependency.DependencyData;
import com.projectomega.main.plugin.loader.dependency.Repository;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private @Nullable DependencyData dependencies;

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
        PluginMeta meta = new PluginMeta(
                requireNonNull(data.get("name"), "Missing 'name' property!").toString(),
                requireNonNull(data.get("main"), "Missing 'main' property!").toString(),
                (String) data.get("author"),
                (String) data.get("description"),
                data.getOrDefault("version", "1.0").toString()
        );
        if (data.containsKey("runtime-dependencies")) {
            Map<String, Object> deps = (Map<String, Object>) data.get("runtime-dependencies");
            List<Repository> repositories = ((List<String>) deps.getOrDefault("repositories", new ArrayList<>()))
                    .stream().map(Repository::new).collect(Collectors.toList());
            repositories.add(Repository.MAVEN_CENTRAL);
            Map<String, String> relocationsMap = (Map<String, String>) deps.getOrDefault("relocations", new LinkedHashMap<>());
            List<Map<String, Object>> dependencies = (List<Map<String, Object>>) deps.getOrDefault("dependencies", new ArrayList<>());
            meta.dependencies = new DependencyData(
                    dependencies.stream().map(dependency -> new Dependency(
                            requireNonNull(dependency.get("groupId"), "groupId").toString(),
                            requireNonNull(dependency.get("artifactId"), "artifactId").toString(),
                            requireNonNull(dependency.get("version"), "version").toString()
                    )).collect(Collectors.toList()),
                    repositories,
                    relocationsMap
            );
        }
        return meta;
    }
}
