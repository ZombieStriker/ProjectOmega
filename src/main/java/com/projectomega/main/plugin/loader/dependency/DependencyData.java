package com.projectomega.main.plugin.loader.dependency;

import com.projectomega.main.plugin.loader.PluginClassLoader;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

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

    public void load(File directory, RelocationHandler relocationHandler, PluginClassLoader classLoader) throws IOException {
        directory.mkdirs();
        for (Dependency dependency : getDependencies()) {
            boolean downloaded = false;
            for (Repository repository : getRepositories()) {
                File f = repository.downloadFile(dependency, directory, this, relocationHandler);
                if (f != null) {
                    classLoader.load(f.toURI().toURL());
                    downloaded = true;
                    break;
                }
            }
            if (!downloaded) {
                StringJoiner repositories = new StringJoiner("\n-", "\n-", "").setEmptyValue("[None]");
                getRepositories().forEach(r -> repositories.add(r.getURL().toString()));
                throw new IllegalArgumentException("Could not download dependency " + dependency.getArtifactId() + " from the specified repositories: " + repositories);
            }
        }
    }

}
