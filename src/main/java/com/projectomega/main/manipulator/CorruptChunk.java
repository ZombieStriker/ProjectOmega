package com.projectomega.main.manipulator;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

@Data
public class CorruptChunk {

    @NotNull
    private final ChunkPos position;

    private @NotNull Date lastModified;
    private byte[] chunkContent;
    private long location;
    private int allocationSize;
    private @Nullable Integer length;
    private @Nullable Integer compression;
    private @NotNull Throwable throwable;

    public CorruptChunk(@NotNull RegionPos regionPos,
                        int index,
                        @NotNull Date lastModified,
                        byte[] chunkContent,
                        long location,
                        int allocationSize,
                        @Nullable Integer length,
                        @Nullable Integer compression,
                        @NotNull Throwable throwable) {
        this(calculateChunkPos(regionPos, index), lastModified, chunkContent, location, allocationSize, length, compression, throwable);
    }

    public CorruptChunk(@NotNull ChunkPos position,
                        @NotNull Date lastModified,
                        byte[] chunkContent,
                        long location,
                        int allocationSize,
                        @Nullable Integer length,
                        @Nullable Integer compression,
                        @NotNull Throwable throwable) {
        this.position = position;
        this.lastModified = lastModified;
        this.chunkContent = chunkContent;
        this.location = location;
        this.allocationSize = allocationSize;
        this.length = length;
        this.compression = compression;
        this.throwable = throwable;
    }

    public static ChunkPos calculateChunkPos(RegionPos regionPos, int index) {
        int offsetX = index % 32;
        int offsetZ = index / 32 % 32;
        int minRegX = regionPos.getXPos() * 32;
        int minRegZ = regionPos.getZPos() * 32;
        int chunkX = minRegX + offsetX;
        int chunkZ = minRegZ + offsetZ;
        return new ChunkPos(chunkX, chunkZ);
    }
}
