package com.projectomega.main;

import com.projectomega.bootstrap.OmegaBootstrap;
import com.projectomega.main.bootstrap.ClassPathAppender;
import com.projectomega.main.bootstrap.JarInJarClassPathAppender;
import com.projectomega.main.plugin.loader.dependency.DependencyData;
import com.projectomega.main.plugin.loader.dependency.Repository;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public class LoaderBootstrap implements OmegaBootstrap {

    private static final DependencyData DEPENDENCY_DATA = DependencyData.builder()

            .repository("https://jitpack.io")

            .dependency("com.google.code.gson:gson:2.8.6")
            .dependency("com.github.TheNullicorn:Nedit:v1.1.1")
            .dependency("org.yaml:snakeyaml:1.28")
            .dependency("com.google.protobuf:protobuf-java:3.17.2")

            .build();

    @Override public void start() {
        ClassPathAppender appender = new JarInJarClassPathAppender(getClass().getClassLoader());
        File libs = new File("libs");
        libs.mkdirs();
        System.out.println(libs.getAbsolutePath());
        try {
            if (!libs.exists() || Objects.requireNonNull(libs.listFiles()).length == 0) {
                System.out.println("Downloading libraries... please wait.");
            }
            URL netty = new URL("https://repo1.maven.org/maven2/io/netty/netty-all/4.1.42.Final/netty-all-4.1.42.Final.jar");
            File file = new File(libs, "netty-all-4.1.24.Final.jar");
            Repository.download(netty, file);
            appender.appendURL(file.toURI().toURL());
            DEPENDENCY_DATA.load(libs, null, appender);
            Main.main();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
