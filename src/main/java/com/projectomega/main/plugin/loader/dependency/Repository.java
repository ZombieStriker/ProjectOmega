package com.projectomega.main.plugin.loader.dependency;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public final class Repository {

    public static final Repository MAVEN_CENTRAL = new Repository("https://repo1.maven.org/maven2/");

    private final URL url;

    public Repository(String url) {
        if (!url.endsWith("/")) url += '/';
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public URL getURL() {
        return url;
    }

    @SneakyThrows
    public URL getPom(@NotNull Dependency dependency) {
        return new URL(url + dependency.getMavenPath() + ".pom");
    }

    @SneakyThrows
    public URL getJAR(@NotNull Dependency dependency) {
        return new URL(url + dependency.getMavenPath() + ".jar");
    }

    @SneakyThrows
    private void download(@NotNull Dependency dependency, File file) {
        if (file.exists()) return;
        try (InputStream is = getJAR(dependency).openStream()) {
            Files.copy(is, file.toPath(), REPLACE_EXISTING);
        }
    }

    public void downloadFile(Dependency dependency, File directory) {
        try {
            for (Dependency transitive : dependency.getTransitiveDependencies(this)) {
                download(transitive, new File(directory, transitive.getName() + ".jar"));
            }
            download(dependency, new File(directory, dependency.getName() + ".jar"));
        } catch (Exception ignored) { // try another repository.
        }
    }

    public File downloadFile(Dependency dependency, File directory, DependencyData data, RelocationHandler handler) {
        try {
            for (Dependency transitive : dependency.getTransitiveDependencies(this)) {
                File input = new File(directory, transitive.getName() + "-unrelocated.jar");
                File output = new File(directory, transitive.getName() + ".jar");
                download(transitive, input);
                handler.remap(input, output, data.getRelocations());
            }
            File input = new File(directory, dependency.getName() + "-unrelocated.jar");
            File output = new File(directory, dependency.getName() + ".jar");
            download(dependency, input);
            handler.remap(input, output, data.getRelocations());
            return output;
        } catch (Exception e) {
            return null; // try another repository.
        }
    }
}
