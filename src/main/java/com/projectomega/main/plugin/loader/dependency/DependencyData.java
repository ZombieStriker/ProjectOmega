package com.projectomega.main.plugin.loader.dependency;

import lombok.Getter;

import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;

@Getter
public class DependencyData {

    private final List<Dependency> dependencies;
    private final List<Repository> repositories;
    private final Map<String, String> relocations;

    public DependencyData(List<Dependency> dependencies, List<Repository> repositories, Map<String, String> relocations) {
        this.dependencies = unmodifiableList(dependencies);
        this.repositories = unmodifiableList(repositories);
        this.relocations = unmodifiableMap(relocations);
    }
}
