package com.projectomega.main.plugin.loader;

import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.game.Omega;
import com.projectomega.main.plugin.OmegaPlugin;
import com.projectomega.main.plugin.PluginMeta;
import com.projectomega.main.plugin.loader.dependency.Relocation;
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

    private final Map<OmegaPlugin, PluginMeta> plugins = new HashMap<>();
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
        Repository.MAVEN_CENTRAL.downloadFile(Relocation.RELOCATOR, librariesFolder);

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
                PluginClassLoader classLoader = new PluginClassLoader(
                        new URL[]{pluginFile.toURI().toURL()},
                        getClass().getClassLoader()
                );
                if (meta.getDependencies() != null) {
                    File directory = new File(pluginsFolder, meta.getName() + File.separator + "libraries");
                    meta.getDependencies().load(directory, relocationHandler, classLoader);
                }
                Class<? extends OmegaPlugin> mainClass = Class
                        .forName(meta.getMainClass(), true, classLoader)
                        .asSubclass(OmegaPlugin.class);
                try {
                    OmegaPlugin plugin = provideInstance(mainClass);
                    classLoader.initialize(plugin);
                    Omega.getLogger().info("Registering plugin class: " + mainClass.getName());
                    plugins.put(plugin, meta);

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
        if(DebuggingUtil.enableDebugPlugin){
            plugins.put(new TestPlugin(), new PluginMeta("Test Plugin","example.com.testplugin.TestPlugin"));
        }
        for (Entry<OmegaPlugin, PluginMeta> entry : plugins.entrySet()) {
            OmegaPlugin plugin = entry.getKey();
            PluginMeta meta = entry.getValue();
            Omega.getLogger().log(Level.INFO, "Enabling " + meta.getName() + ": " + meta.getVersion());
            plugin.onEnable();
        }
    }

    public void disablePlugins() {
        for (OmegaPlugin plugin : plugins.keySet()) {
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
