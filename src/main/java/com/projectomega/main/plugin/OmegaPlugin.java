package com.projectomega.main.plugin;

import com.projectomega.main.plugin.loader.*;
import org.jetbrains.annotations.*;

public abstract class OmegaPlugin {

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

}
