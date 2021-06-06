package com.projectomega.main.bootstrap;

import org.jetbrains.annotations.NotNull;

import java.net.URL;

public interface ClassPathAppender extends AutoCloseable {

    void appendURL(@NotNull URL url);

}
