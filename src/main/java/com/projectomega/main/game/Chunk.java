package com.projectomega.main.game;

public class Chunk {

    private Block[][][] blocks = new Block[16][256][16];
    private World world;
    private int x;
    private int z;

    public Chunk(World world, int x, int z) {
        this.world = world;
        this.x=  x;
        this.z = z;
        int xoff = x * 16;
        int zoff = z * 16;
        for (int x1 = 0; x1 < 16; x1++) {
            for (int z1 = 0; z1 < 16; z1++) {
                for (int y1 = 0; y1 < 256; y1++) {
                    blocks[x1][y1][z1] = new Block(Location.at(xoff + x1, y1, zoff + z1, world));
                }
            }
        }
    }

    public World getWorld() {
        return world;
    }

    public Block getBlockAtChunkRelative(int x, int y, int z) {
        return blocks[x][y][z];
    }

    public Block getBlockAt(int x, int y, int z) {
        int xmod = Math.abs(x) % 16;
        int zmod = Math.abs(z) % 16;
        if (x < 0)
            xmod = -xmod;
        if (z < 0)
            zmod = -zmod;
        return blocks[xmod][y][zmod];
    }

    public int getX() {
        return x;
    }
    public int getZ(){return z;}
}
