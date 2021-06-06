package com.projectomega.main.plugin;

import com.projectomega.main.command.ExecutableCommand;
import com.projectomega.main.game.Omega;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public abstract class OmegaPlugin {

    private boolean initialized;
    private PluginMeta pluginMeta;
    private File dataDirectory;
    private PluginClassLoader classLoader;

    public void onLoad() {
    }

    public abstract void onEnable();

    public abstract void onDisable();

    @Nullable
    public static OmegaPlugin getPlugin(@NotNull Class<?> clazz) {
        ClassLoader loader = clazz.getClassLoader();
        if (loader instanceof PluginClassLoader) {
            return ((PluginClassLoader) loader).getPlugin();
        }
        return null;
    }

    void initialize(@NonNull PluginMeta pluginMeta, @NonNull File dataDirectory, PluginClassLoader classLoader) {
        if (initialized) throw new IllegalStateException("Plugin " + pluginMeta.getName() + " is already initialized!");
        initialized = true;
        this.pluginMeta = pluginMeta;
        this.dataDirectory = dataDirectory;
        this.classLoader = classLoader;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public PluginMeta getPluginMeta() {
        return pluginMeta;
    }

    public File getDataDirectory() {
        return dataDirectory;
    }

    public PluginClassLoader getClassLoader() {
        return classLoader;
    }

    public void registerCommand(@NonNull ExecutableCommand command) {
        Omega.getCommandHandler().register(this, command);
    }

}
