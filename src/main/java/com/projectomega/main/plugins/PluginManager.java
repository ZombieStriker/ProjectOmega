package com.projectomega.main.plugins;

import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.game.Omega;
import example.com.testplugin.TestPlugin;
import org.apache.xbean.finder.ResourceFinder;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

public class PluginManager {

    private static List<OmegaPlugin> plugins = new ArrayList<>();

    public static void init() {
        if (DebuggingUtil.enableTestPlugin) {
            registerPluginClass(TestPlugin.class);
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

    public static void registerPlugins(File pluginFolder) {
        for (File file : pluginFolder.listFiles()) {
            if (file.getName().endsWith(".jar")) {
                Omega.getLogger().log(Level.INFO,"Found Jar "+file.getName());
                JarFile jar = null;
                InputStream stream = null;
                try {

                        jar = new JarFile(file);
                        JarEntry entry = jar.getJarEntry("omega.yml");

                        if (entry == null) {
                            Omega.getLogger().log(Level.INFO,"Could not find omega.yml");
                            continue;
                        }

                        stream = jar.getInputStream(entry);

                        Yaml yaml = new Yaml();
                        HashMap<String,Object> map = yaml.load(stream);

                        if(!map.containsKey("main")){
                            Omega.getLogger().log(Level.INFO,"Could not find main entry in omega.yml");
                            continue;
                        }

                    ResourceFinder resourceFinder = new ResourceFinder((String) map.get("main"),file.toURL());
                    //List<Class> implem = resourceFinder.findAllImplementations(OmegaPlugin.class);
                    try {
                        URLClassLoader classloader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()}, PluginManager.class.getClassLoader());
                        Class<?> jarclass = Class.forName((String) map.get("main"),true,classloader);
                        Class<? extends  OmegaPlugin> omegaplugin = jarclass.asSubclass(OmegaPlugin.class);
                        Omega.getLogger().log(Level.INFO,"Registering Plugin Class: -"+ omegaplugin.getName());
                        registerPluginClass(omegaplugin);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (jar != null) {
                        try {
                            jar.close();
                        } catch (IOException e) {
                        }
                    }
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                        }
                    }
                }

                //JarFile jar = new JarFile(file);
            }
        }
    }

    public static void enableAllPlugins() {
        for (OmegaPlugin plugin : plugins) {
            Omega.getLogger().log(Level.INFO,"Enabling "+plugin.getName()+":"+plugin.getVersion());
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
