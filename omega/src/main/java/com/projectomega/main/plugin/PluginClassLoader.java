package com.projectomega.main.plugin;

import com.projectomega.main.bootstrap.ClassPathAppender;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.net.URLClassLoader;

public final class PluginClassLoader extends URLClassLoader implements ClassPathAppender {

    static {
        ClassLoader.registerAsParallelCapable();
    }

    private OmegaPlugin plugin;

    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public void appendURL(@NotNull URL url) {
        addURL(url); // we could use that to allow dependencies at runtime.
    }

    void initialize(OmegaPlugin plugin) {
        this.plugin = plugin;
    }

    public OmegaPlugin getPlugin() {
        return plugin;
    }

}
