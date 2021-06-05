package com.projectomega.main.manipulator;

import java.util.Objects;

public final class ChunkPos {

    private final int xPos;
    private final int zPos;

    public ChunkPos(int xPos, int zPos) {
        this.xPos = xPos;
        this.zPos = zPos;
    }

    public int getXPos() {
        return xPos;
    }

    public int getZPos() {
        return zPos;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChunkPos)) return false;
        ChunkPos chunkPos = (ChunkPos) o;
        return xPos == chunkPos.xPos && zPos == chunkPos.zPos;
    }

    @Override public int hashCode() {
        return Objects.hash(xPos, zPos);
    }

    @Override public String toString() {
        return String.format("ChunkPos{xPos=%d, zPos=%d}", xPos, zPos);
    }
}
