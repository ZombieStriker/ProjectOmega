package com.projectomega.main.game;

import java.util.HashMap;
import java.util.Map;

public class Region {

    public HashMap<ChunkPosition, Chunk> chunks = new HashMap<>();

    private int x;
    private int z;
    private World world;

    public Region(World world,int x, int z){
        this.x = x;
        this.z = z;
        this.world= world;
    }
    public int getX(){
        return x;
    }
    public int getZ(){
        return z;
    }

    public Chunk getOrLoadChunk(int x, int z){
        Chunk chunk = getLoadedChunk(x,z);
        if(chunk!=null)
            return chunk;
        return createChunk(x,z);
    }

    public Chunk getLoadedChunk(int x, int z) {
        for(Map.Entry<ChunkPosition, Chunk> c : chunks.entrySet()){
            if(c.getKey().getX()==x&&c.getKey().getZ()==z)
                return c.getValue();
        }
        return null;
    }

    public Chunk createChunk(int x, int z) {
        Chunk chunk = new Chunk(world,x,z);
        chunks.put(new ChunkPosition(x,z),chunk);
        return chunk;
    }
}
