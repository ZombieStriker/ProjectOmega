package com.projectomega.bootstrap;

import java.util.Collections;
import java.util.List;

public final class Bootstrap {

    private static final List<String> MODULES = Collections.singletonList("omega.isolated-jar");
    private static final String MAIN_CLASS = "com.projectomega.main.LoaderBootstrap";

    public static void main(String[] args) {
        JarInJarClassLoader loader = new JarInJarClassLoader(Bootstrap.class.getClassLoader(), MODULES);
        OmegaBootstrap bootstrap = loader.instantiate(MAIN_CLASS);
        bootstrap.start();
    }

}
