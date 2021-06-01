package com.projectomega.main.plugin.loader;

import com.projectomega.main.plugin.OmegaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the strategy in which a plugin can be instantiated.
 * <p>
 * For more information on using, see {@link PluginProvider}.
 */
public interface PluginInstanceProvider {

    /**
     * Returns the plugin instance
     *
     * @param pluginClass The plugin class
     * @return The plugin instance. Must not be null.
     * @throws Throwable Any exceptions
     */
    @NotNull OmegaPlugin getInstance(@NotNull Class<? extends OmegaPlugin> pluginClass) throws Throwable;

}
