package com.projectomega.main.manipulator;

import me.nullicorn.nedit.type.NBTCompound;

import java.util.Date;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public final class Chunk {

    private final Date lastModified;
    private final NBTCompound compound;
    private final ChunkPos position;

    public Chunk(Date lastModified, NBTCompound compound) {
        this.lastModified = lastModified;
        this.compound = compound;
        int x = requireNonNull((Integer) compound.get("xPos"), "'xPos' property is missing");
        int z = requireNonNull((Integer) compound.get("zPos"), "'zPos' property is missing");
        this.position = new ChunkPos(x, z);
    }

    public int getDataVersion() {
        return requireNonNull((Integer) compound.get("DataVersion"), "'DataVersion' property is missing");
    }

    public int getLevel() {
        return requireNonNull((Integer) compound.get("Level"), "'Level' property is missing");
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
