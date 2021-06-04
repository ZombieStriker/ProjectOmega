package com.projectomega.main.plugin;

import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.game.Omega;
import com.projectomega.main.plugin.loader.PluginInstanceProvider;
import com.projectomega.main.plugin.loader.PluginProvider;
import com.projectomega.main.plugin.loader.dependency.RelocationHandler;
import com.projectomega.main.plugin.loader.dependency.Repository;
import example.com.testplugin.TestPlugin;
import lombok.NonNull;
import lombok.SneakyThrows;
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
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import static java.util.Objects.requireNonNull;

public class PluginManager {

    private final Map<String, OmegaPlugin> plugins = new HashMap<>();
    private final RelocationHandler relocationHandler;
    private final File librariesFolder;
    private final File pluginsFolder;

    @SneakyThrows
    public PluginManager() {
        File serverDir = Omega.getServerDirectory();
        librariesFolder = new File(serverDir, "libs");
        librariesFolder.mkdirs();
        pluginsFolder = new File(serverDir, "plugins");
        pluginsFolder.mkdirs();
        Repository.MAVEN_CENTRAL.downloadFile(RelocationHandler.RELOCATOR, librariesFolder);

        // load libs
        File[] libraryFiles = requireNonNull(librariesFolder.listFiles());
        URL[] urls = new URL[libraryFiles.length];
        for (int i = 0; i < libraryFiles.length; i++) urls[i] = libraryFiles[i].toURI().toURL();
        relocationHandler = new RelocationHandler(urls);
    }

    public void searchPlugins() {
        for (File file : requireNonNull(pluginsFolder.listFiles(f -> f.getName().endsWith(".jar")))) {
            loadPlugin(file);
        }
    }

    @SneakyThrows
    public void loadPlugin(@NonNull File pluginFile) {
        Omega.getLogger().info("Found JAR " + pluginFile.getName());
        try (JarFile jar = new JarFile(pluginFile)) {
            JarEntry entry = jar.getJarEntry("omega.yml");
            if (entry == null) {
                throw new IllegalStateException("Plugin file " + pluginFile.getName() + " does not contain omega.yml.");
            }
            try (InputStream stream = jar.getInputStream(entry)) {
                PluginMeta meta = PluginMeta.read(stream);
                if (plugins.containsKey(meta.getName())) {
                    throw new IllegalStateException("Plugin with name '" + meta.getName() + "' already exists!");
                }
                File dataFolder = new File(pluginsFolder, meta.getName());
                PluginClassLoader classLoader = new PluginClassLoader(
                        new URL[]{pluginFile.toURI().toURL()},
                        getClass().getClassLoader()
                );
                if (meta.getDependencies() != null) {
                    File directory = new File(dataFolder, "libraries");
                    meta.getDependencies().load(directory, relocationHandler, classLoader);
                }
                Class<? extends OmegaPlugin> mainClass = Class
                        .forName(meta.getMainClass(), true, classLoader)
                        .asSubclass(OmegaPlugin.class);
                try {
                    OmegaPlugin plugin = provideInstance(mainClass);
                    classLoader.initialize(plugin);
                    plugin.initialize(meta, dataFolder, classLoader);
                    Omega.getLogger().info("Registering plugin class: " + mainClass.getName());
                    plugins.put(meta.getName(), plugin);

                    plugin.onLoad();
                } catch (Throwable t) {
                    Omega.getLogger().log(Level.WARNING, "Could not load plugin from " + mainClass, t);
                }
            }
        } catch (IOException t) {
            Omega.getLogger().log(Level.WARNING, "Failed to load plugin from file " + pluginFile.getName(), t);
        }
    }

    public void enablePlugins() {
        if (DebuggingUtil.enableDebugPlugin) {
            OmegaPlugin plugin = new TestPlugin();
            PluginMeta meta = new PluginMeta("TestPlugin", TestPlugin.class.getName());
            plugin.initialize(meta, new File(pluginsFolder, meta.getName()), null);

            plugins.put(meta.getName(), plugin);
        }
        for (Entry<String, OmegaPlugin> entry : plugins.entrySet()) {
            String name = entry.getKey();
            OmegaPlugin plugin = entry.getValue();
            Omega.getLogger().log(Level.INFO, "Enabling " + name + ": " + plugin.getPluginMeta().getVersion());
            plugin.onEnable();
        }
    }

    public void disablePlugins() {
        for (OmegaPlugin plugin : plugins.values()) {
            plugin.onDisable();
        }
    }

    private OmegaPlugin provideInstance(@NotNull Class<? extends OmegaPlugin> mainClass) throws Throwable {
        if (mainClass.isAnnotationPresent(PluginProvider.class)) {
            PluginInstanceProvider provider = mainClass.getAnnotation(PluginProvider.class).value().newInstance();
            return requireNonNull(provider.getInstance(mainClass), "provider must not return a null value!");
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
}
