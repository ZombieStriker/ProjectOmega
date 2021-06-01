package com.projectomega.main.plugin.loader.dependency;

public final class Relocation {

    private final String pattern;
    private final String relocatedPattern;

    public Relocation(String pattern, String relocatedPattern) {
        this.pattern = pattern;
        this.relocatedPattern = relocatedPattern;
    }

    public String getPattern() {
        return pattern;
    }

    public String getRelocatedPattern() {
        return relocatedPattern;
    }
}
