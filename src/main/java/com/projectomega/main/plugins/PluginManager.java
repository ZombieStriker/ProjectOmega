package com.projectomega.main.plugins;

import com.projectomega.main.debugging.DebuggingUtil;
import example.com.testplugin.TestPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginManager {

    private static List<OmegaPlugin> plugins = new ArrayList<>();

    public static void init(){
        if(DebuggingUtil.enableTestPlugin){
            registerPluginClass(TestPlugin.class);
        }
    }
    public static void registerPlugins(File pluginFolder){
        //TODO: register plugin file page
    }

    public static void enableAllPlugins(){
        for(OmegaPlugin plugin : plugins){
            plugin.onEnable();
        }
    }
    public static void disableAllPlugins(){
        for(OmegaPlugin plugin : plugins){
            plugin.onDisable();
        }
    }

    private static void registerPluginClass(Class classInstance){
        try {
            OmegaPlugin plugin = (OmegaPlugin) classInstance.newInstance();
            plugins.add(plugin);
        } catch (ClassCastException e){
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
