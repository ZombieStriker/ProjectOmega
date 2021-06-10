
package com.projectomega.bootstrap;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/**
 * Classloader that can load a jar from within another jar file.
 *
 * <p>The "loader" jar contains the loading code, and is class-loaded by the
 * platform.</p>
 *
 * <p>The inner "plugin" jar contains the plugin itself, and is class-loaded
 * by the loading code & this classloader.</p>
 */
public class JarInJarClassLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    /**
     * Creates a new jar-in-jar class loader.
     *
     * @param loaderClassLoader the loader plugin's classloader (setup and created by the platform)
     * @param paths             the paths to the jar-in-jar resource within the loader jar
     * @throws LoadingException if something unexpectedly bad happens
     */
    public JarInJarClassLoader(ClassLoader loaderClassLoader, List<String> paths) throws LoadingException {
        super(paths.stream().map(p -> extractJar(loaderClassLoader, p)).toArray(URL[]::new), loaderClassLoader);
    }

    public void addJarToClasspath(URL url) {
        addURL(url);
    }

    public void deleteJarResource() {
        URL[] urls = getURLs();
        if (urls.length == 0) {
            return;
        }

        try {
            Path path = Paths.get(urls[0].toURI());
            Files.deleteIfExists(path);
        } catch (Exception e) {
            // ignore
        }
    }

    /**
     * Creates a new bootstrap instance.
     *
     * @param bootstrapClass the name of the bootstrap plugin class
     * @return the instantiated bootstrap plugin
     */
    public OmegaBootstrap instantiate(String bootstrapClass,
                                      @NotNull File runDirectory) throws LoadingException {
        Class<?> bootstrap;
        try {
            bootstrap = loadClass(bootstrapClass);
        } catch (ReflectiveOperationException e) {
            throw new LoadingException("Unable to load bootstrap class", e);
        }

        Constructor<?> constructor;
        try {
            constructor = bootstrap.getConstructor(File.class);
        } catch (ReflectiveOperationException e) {
            throw new LoadingException("Unable to get bootstrap constructor", e);
        }

        try {
            return (OmegaBootstrap) constructor.newInstance(runDirectory);
        } catch (ReflectiveOperationException e) {
            throw new LoadingException("Unable to create bootstrap plugin instance", e);
        }
    }

    /**
     * Extracts the "jar-in-jar" from the loader plugin into a temporary file,
     * then returns a URL that can be used by the {@link JarInJarClassLoader}.
     *
     * @param loaderClassLoader the classloader for the "host" loader plugin
     * @param jarResourcePath   the inner jar resource path
     * @return a URL to the extracted file
     */
    private static URL extractJar(ClassLoader loaderClassLoader, String jarResourcePath) throws LoadingException {
        // get the jar-in-jar resource
        URL jarInJar = loaderClassLoader.getResource(jarResourcePath);
        if (jarInJar == null) {
            try {
                jarInJar = new File("omega/build/libs/" + jarResourcePath).toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            if (jarInJar == null)
                throw new LoadingException("Could not locate jar-in-jar");
        }

        // create a temporary file
        // on posix systems by default this is only read/writable by the process owner
        Path path;
        try {
            path = Files.createTempFile("omega-jar", ".jar.tmp");
        } catch (IOException e) {
            throw new LoadingException("Unable to create a temporary file", e);
        }

        // mark that the file should be deleted on exit
        path.toFile().deleteOnExit();

        // copy the jar-in-jar to the temporary file path
        try (InputStream in = jarInJar.openStream()) {
            Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new LoadingException("Unable to copy jar-in-jar to temporary path", e);
        }

        try {
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new LoadingException("Unable to get URL from path", e);
        }
    }

}
