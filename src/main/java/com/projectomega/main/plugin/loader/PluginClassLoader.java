package com.projectomega.main.plugin.loader;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginClassLoader extends URLClassLoader {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public void load(@NotNull URL url) {
        addURL(url); // we could use that to allow dependencies at runtime.
    }
}
