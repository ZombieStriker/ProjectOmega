package com.projectomega.main;

import com.projectomega.bootstrap.OmegaBootstrap;
import com.projectomega.main.bootstrap.ClassPathAppender;
import com.projectomega.main.bootstrap.JarInJarClassPathAppender;
import com.projectomega.main.plugin.loader.dependency.DependencyData;

import java.io.File;
import java.util.Objects;

public class LoaderBootstrap implements OmegaBootstrap {

    private static final DependencyData DEPENDENCY_DATA = DependencyData.builder()

            .repository("https://jitpack.io")

            .dependencyFromURL( // download netty from a URL
                    "https://repo1.maven.org/maven2/io/netty/netty-all/4.1.42.Final/netty-all-4.1.42.Final.jar",
                    "netty-all",
                    "4.1.42-Final"
            )

            .dependency("com.google.code.gson", "gson", "2.8.6")
            .dependency("com.github.TheNullicorn", "Nedit", "v1.1.1")
            .dependency("org.yaml", "snakeyaml", "1.28")

            .build();

    @Override public void start() {
        ClassPathAppender appender = new JarInJarClassPathAppender(getClass().getClassLoader());
        File libsFolder = new File("libs");
        libsFolder.mkdirs();
        try {
            if (!libsFolder.exists() || Objects.requireNonNull(libsFolder.listFiles()).length == 0) {
                System.out.println("Downloading libraries... please wait.");
            }
            DEPENDENCY_DATA.load(libsFolder, null, appender);
            Main.main();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
