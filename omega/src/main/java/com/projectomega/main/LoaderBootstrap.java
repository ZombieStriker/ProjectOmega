package com.projectomega.main;

import com.projectomega.bootstrap.OmegaBootstrap;
import com.projectomega.main.bootstrap.ClassPathAppender;
import com.projectomega.main.bootstrap.JarInJarClassPathAppender;
import com.projectomega.main.plugin.loader.dependency.DependencyData;
import com.projectomega.main.plugin.loader.dependency.Repository;
import lombok.NonNull;

import java.io.File;
import java.net.URL;
import java.util.Objects;

public class LoaderBootstrap implements OmegaBootstrap {

    private static final DependencyData DEPENDENCY_DATA = DependencyData.builder()

            .repository("https://jitpack.io")

            .dependency("com.google.code.gson:gson:2.8.6")
            .dependency("com.github.TheNullicorn:Nedit:v1.1.1")
            .dependency("org.yaml:snakeyaml:1.28")

            .build();

    @Override public void start() {
        ClassPathAppender appender = new JarInJarClassPathAppender(getClass().getClassLoader());
        File libsFolder = new File("libs");
        libsFolder.mkdirs();
        try {
            if (!libsFolder.exists() || Objects.requireNonNull(libsFolder.listFiles()).length == 0) {
                System.out.println("Downloading libraries... please wait.");
            }
            downloadNetty(libsFolder, appender);
            DEPENDENCY_DATA.load(libsFolder, null, appender);
            Main.main();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    // we download netty specifically because we only need the full JAR
    private void downloadNetty(File libsFolder, @NonNull ClassPathAppender appender) throws Exception {
        URL netty = new URL("https://repo1.maven.org/maven2/io/netty/netty-all/4.1.42.Final/netty-all-4.1.42.Final.jar");
        File file = new File(libsFolder, "netty-all-4.1.24.Final.jar");
        Repository.download(netty, file);
        appender.appendURL(file.toURI().toURL());
    }

}
