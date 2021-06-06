package com.projectomega.main.plugin.loader;

import com.projectomega.main.plugin.OmegaPlugin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation to mark the way a plugin will be instantiated.
 * <p>
 * This annotation should be added to classes that extend {@link OmegaPlugin},
 * and should provide the {@link PluginInstanceProvider} which will be used
 * to fetch the plugin instance, whether it be a singleton, a custom constructor, or whatnot.
 * <p>
 * When this annotation is not present, instantiation will be attempted in the
 * following order:
 * <ol>
 *     <li>A singleton method: A static method that returns the plugin type, and has no parameters</li>
 *     <li>A singleton field: A static field that matches the plugin type</li>
 *     <li>A no-args contructor</li>
 * </ol>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PluginProvider {

    /**
     * The plugin instance provider class. It <strong>must</strong> have a no-arg constructor.
     *
     * @return The plugin provider value
     */
    Class<? extends PluginInstanceProvider> value();

}
