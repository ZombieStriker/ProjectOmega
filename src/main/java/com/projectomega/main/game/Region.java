package com.projectomega.main.game;

import java.util.HashMap;
import java.util.Map;

public class Region {

    public HashMap<ChunkPosition, Chunk> chunks = new HashMap<>();

    private int x;
    private int z;

    public Region(int x, int z){
        this.x = x;
        this.z = z;
    }
    public int getX(){
        return x;
    }
    public int getZ(){
        return z;
    }

    public Chunk getLoadedChunk(int i, int i1) {
        for(Map.Entry<ChunkPosition, Chunk> c : chunks.entrySet()){
            if(c.getKey().getX()==i&&c.getKey().getZ()==i1)
                return c.getValue();
        }
        return null;
    }
}
