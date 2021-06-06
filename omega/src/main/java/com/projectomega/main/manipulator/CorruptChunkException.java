package com.projectomega.main.manipulator;

import org.jetbrains.annotations.NotNull;

public final class CorruptChunkException extends RuntimeException {

    private final CorruptChunk chunk;

    public CorruptChunkException(@NotNull CorruptChunk chunk) {
        super("The chunk " + chunk.getPosition() + " is corrupt!", chunk.getThrowable());
        this.chunk = chunk;
    }

    public @NotNull CorruptChunk getChunk() {
        return this.chunk;
    }
}
