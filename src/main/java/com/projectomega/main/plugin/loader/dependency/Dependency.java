package com.projectomega.main.plugin.loader.dependency;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Represents a runtime-downloaded dependency
 */
@EqualsAndHashCode @ToString
public class Dependency {

    private static final String MAVEN_CENTRAL = "https://repo1.maven.org/maven2/";

    private final String groupId, artifactId, version, repository;
    private final Map<String, String> relocations;

    public Dependency(String groupId, String artifactId, String version, String repository, Map<String, String> relocations) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.repository = repository == null ? MAVEN_CENTRAL : !repository.endsWith("/") ? repository + '/' : repository;
        this.relocations = relocations;
    }

    public Dependency(String groupId, String artifactId, String version, String repository) {
        this(groupId, artifactId, version, repository, Collections.emptyMap());
    }

    public Dependency(String groupId, String artifactId, String version) {
        this(groupId, artifactId, version, MAVEN_CENTRAL);
    }

    public @Unmodifiable List<Relocation> getRelocations() {
        List<Relocation> relocations = new ArrayList<>();
        this.relocations.forEach((pattern, relocatedPattern) -> relocations.add(new Relocation(pattern, relocatedPattern)));
        return relocations;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getRepository() {
        return repository;
    }
}
