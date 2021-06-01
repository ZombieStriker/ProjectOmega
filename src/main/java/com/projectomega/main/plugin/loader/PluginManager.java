package com.projectomega.main.plugin.loader;

import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.game.Omega;
import com.projectomega.main.plugin.OmegaPlugin;
import com.projectomega.main.plugin.PluginMeta;
import example.com.testplugin.TestPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class PluginManager {

    private static final PluginInstanceProvider NO_ARG = Class::newInstance;

    private static final PluginInstanceProvider SINGLETON = pluginClass -> {
        for (Method method : pluginClass.getDeclaredMethods()) {
            if (!Modifier.isStatic(method.getModifiers())) continue;
            if (method.getReturnType() != pluginClass || method.getParameterCount() != 0) continue;
            if (!method.isAccessible()) method.setAccessible(true);
            return (OmegaPlugin) method.invoke(null);
        }
        for (Field field : pluginClass.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers())) continue;
            if (!field.isAccessible()) field.setAccessible(true);
            return (OmegaPlugin) field.get(null);
        }
        throw new IllegalStateException("Cannot find a singleton field or method.");
    };

    private final Map<OmegaPlugin, PluginMeta> plugins = new HashMap<>();

    public void searchPlugins() {
        if (DebuggingUtil.enableTestPlugin) {
            plugins.put(new TestPlugin(), new PluginMeta("Test Plugin", TestPlugin.class.getName()));
        }
        File jarPath = Omega.getJarFile();
        if (jarPath != null) {
            File pluginfolder = new File(jarPath.getParent(), "/plugins");
            if (!pluginfolder.exists()) {
                pluginfolder.mkdirs();
                pluginfolder = new File(jarPath.getParent(), "/plugins");
            }
            registerPlugins(pluginfolder);
        }
    }

    public void registerPlugins(File pluginFolder) {
        for (File file : pluginFolder.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                Omega.getLogger().log(Level.INFO, "Found Jar " + file.getName());
                JarFile jar = null;
                InputStream stream = null;
                try {
                    jar = new JarFile(file);
                    JarEntry entry = jar.getJarEntry("omega.yml");

                    if (entry == null) {
                        Omega.getLogger().log(Level.INFO, "Could not find omega.yml");
                        continue;
                    }

                    stream = jar.getInputStream(entry);

                    PluginMeta meta;

                    try {
                        meta = PluginMeta.read(stream);
                    } catch (Throwable t) {
                        Omega.getLogger().log(Level.WARNING, "Could not load omega.yml from " + file.getName(), t);
                        continue;
                    }

                    try {
                        PluginClassLoader classLoader = new PluginClassLoader(
                                new URL[]{file.toURI().toURL()},
                                PluginManager.class.getClassLoader()
                        );

                        Class<? extends OmegaPlugin> mainClass = Class.forName(meta.getMainClass(), true, classLoader)
                                .asSubclass(OmegaPlugin.class);
                        try {
                            OmegaPlugin plugin = getPlugin(mainClass);
                            Omega.getLogger().log(Level.INFO, "Registering plugin class: -" + mainClass.getName());
                            plugins.put(plugin, meta);
                        } catch (Throwable t) {
                            Omega.getLogger().log(Level.WARNING, "Could not load plugin from " + mainClass, t);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (jar != null) {
                        try {
                            jar.close();
                        } catch (IOException ignored) {
                        }
                    }
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException ignored) {
                        }
                    }
                }
            }
        }
    }

    public void enableAllPlugins() {
        for (Entry<OmegaPlugin, PluginMeta> entry : plugins.entrySet()) {
            OmegaPlugin plugin = entry.getKey();
            PluginMeta meta = entry.getValue();
            Omega.getLogger().log(Level.INFO, "Enabling " + meta.getName() + ": " + meta.getVersion());
            plugin.onEnable();
        }
    }

    public void disableAllPlugins() {
        for (OmegaPlugin plugin : plugins.keySet()) {
            plugin.onDisable();
        }
    }

    private OmegaPlugin getPlugin(@NotNull Class<? extends OmegaPlugin> mainClass) throws Throwable {
        if (mainClass.isAnnotationPresent(PluginProvider.class)) {
            PluginInstanceProvider provider = mainClass.getAnnotation(PluginProvider.class).value().newInstance();
            return Objects.requireNonNull(provider.getInstance(mainClass), "provider must not return a null value!");
        } else {
            try {
                return SINGLETON.getInstance(mainClass);
            } catch (Throwable t) {
                try {
                    return NO_ARG.getInstance(mainClass);
                } catch (Throwable th) {
                    throw new IllegalStateException("Cannot find a way to create a plugin instance for class " + mainClass + ". " +
                            "Either define a no-arg constructor, create a static (final) instance, or provide a custom provider with @PluginProvider.");
                }
            }
        }
    }
}