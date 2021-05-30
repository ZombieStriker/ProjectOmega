package com.projectomega.main.plugins;

public abstract class OmegaPlugin {

    public abstract String getName();

    public abstract String getVersion();

    public abstract void onEnable();

    public abstract void onDisable();

    protected OmegaPlugin(){

    }
}
