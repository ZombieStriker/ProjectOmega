package com.projectomega.bootstrap;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The bootstrap class. This is invoked by the platform.
 */
public final class Bootstrap {

    /**
     * The list of modules that should be extracted. We can add names of modules here
     */
    private static final List<String> MODULES = Stream.of("omega")
            .map(s -> s + ".isolated-jar")
            .collect(Collectors.toList());

    /**
     * The main class name which implements {@link OmegaBootstrap}
     */
    private static final String MAIN_CLASS = "com.projectomega.main.LoaderBootstrap";

    public static void main(String[] args) throws Throwable {
        File parent = new File("").getAbsoluteFile();
        JarInJarClassLoader loader = new JarInJarClassLoader(Bootstrap.class.getClassLoader(), MODULES);
        OmegaBootstrap bootstrap = loader.instantiate(MAIN_CLASS, parent);
        bootstrap.start();
    }

}
