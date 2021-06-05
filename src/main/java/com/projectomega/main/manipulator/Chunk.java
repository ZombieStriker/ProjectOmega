package com.projectomega.main.manipulator;

import me.nullicorn.nedit.type.NBTCompound;

import java.util.Date;
import java.util.Objects;

public final class Chunk {

    private final Date lastModified;
    private final NBTCompound compound;
    private final ChunkPos position;

    public Chunk(Date lastModified, NBTCompound compound) {
        this.lastModified = lastModified;
        this.compound = compound;
        this.position = new ChunkPos(compound.getInt("xPos", 0), compound.getInt("zPos", 0));
    }

    public int getDataVersion() {
        return compound.getInt("DataVersion", 0);
    }

    public int getLevel() {
        return compound.getInt("Level", 0);
    }

    public Date getLastModified() {
        return lastModified;
    }

    public NBTCompound getCompound() {
        return compound;
    }

    public ChunkPos getPosition() {
        return position;
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chunk)) return false;
        Chunk chunk = (Chunk) o;
        return Objects.equals(lastModified, chunk.lastModified) && Objects.equals(compound, chunk.compound) && Objects.equals(position, chunk.position);
    }

    @Override public int hashCode() {
        return Objects.hash(lastModified, compound, position);
    }

    @Override public String toString() {
        return String.format("Chunk{lastModified=%s, compound=%s, position=%s}", lastModified, compound, position);
    }
}
