package com.projectomega.main.bootstrap;


import com.projectomega.bootstrap.JarInJarClassLoader;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;

public class JarInJarClassPathAppender implements ClassPathAppender {

    private final JarInJarClassLoader classLoader;

    public JarInJarClassPathAppender(ClassLoader classLoader) {
        if (!(classLoader instanceof JarInJarClassLoader)) {
            throw new IllegalArgumentException("Loader is not a JarInJarClassLoader: " + classLoader.getClass().getName());
        }
        this.classLoader = (JarInJarClassLoader) classLoader;
    }

    @Override public void appendURL(@NotNull URL url) {
        this.classLoader.addJarToClasspath(url);
    }


    @Override
    public void close() {
        this.classLoader.deleteJarResource();
        try {
            this.classLoader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
