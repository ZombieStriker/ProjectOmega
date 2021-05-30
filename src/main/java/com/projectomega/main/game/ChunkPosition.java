package com.projectomega.main.game;

import java.util.Objects;

public class ChunkPosition {

    private int x;
    private int z;

    public ChunkPosition(int x, int z) {
        this.x = x;
        this.z = z;
    }

    public int getRegionX(){
        return (getX()/32) +(getX()<0?-1:0);
    }
    public int getRegionZ(){
        return (getZ()/32) +(getZ()<0?-1:0);
    }

    public int getX(){
        return x;
    }
    public int getZ(){
        return z;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChunkPosition that = (ChunkPosition) o;
        return x == that.x && z == that.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, z);
    }
}
