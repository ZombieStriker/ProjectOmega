package com.projectomega.main.plugins;

import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.game.Omega;
import example.com.testplugin.TestPlugin;
import org.apache.xbean.finder.ResourceFinder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;

public class PluginManager {

    private static List<OmegaPlugin> plugins = new ArrayList<>();

    public static void init() {
        if (DebuggingUtil.enableTestPlugin) {
            registerPluginClass(TestPlugin.class);
        }
        File jarPath = null;
        try {
            URI uri = Omega.class.getProtectionDomain().getCodeSource().getLocation().toURI();
            if (uri != null) ;
            jarPath = new File(uri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (jarPath != null) {
            File pluginfolder = new File(jarPath.getParent(), "/plugins");
            if (!pluginfolder.exists()) {
                pluginfolder.mkdirs();
                pluginfolder = new File(jarPath.getParent(), "/plugins");
            }
            registerPlugins(pluginfolder);
        }
    }

    public static void registerPlugins(File pluginFolder) {
        for (File file : pluginFolder.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                try {
                    ResourceFinder resourceFinder = new ResourceFinder("omega.yml", file.toURL());
                    List<Class> implem = resourceFinder.findAllImplementations(OmegaPlugin.class);
                    for (Class c : implem) {
                        registerPluginClass(c);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                //JarFile jar = new JarFile(file);
            }
        }
    }

    public static void enableAllPlugins() {
        for (OmegaPlugin plugin : plugins) {
            plugin.onEnable();
        }
    }

    public static void disableAllPlugins() {
        for (OmegaPlugin plugin : plugins) {
            plugin.onDisable();
        }
    }

    private static void registerPluginClass(Class classInstance) {
        try {
            OmegaPlugin plugin = (OmegaPlugin) classInstance.newInstance();
            plugins.add(plugin);
        } catch (ClassCastException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
