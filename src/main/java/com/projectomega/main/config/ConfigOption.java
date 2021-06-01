package com.projectomega.main.config;

public class ConfigOption {

    private int indent = 4;
    private String pathSeparator = ".";

    public int getIndent() {
        return indent;
    }

    public String getPathSeparator() {
        return pathSeparator;
    }

    public ConfigOption setIndent(int indent) {
        this.indent = indent;
        return this;
    }

    public ConfigOption setPathSeparator(String pathSeparator) {
        this.pathSeparator = pathSeparator;
        return this;
    }
}
