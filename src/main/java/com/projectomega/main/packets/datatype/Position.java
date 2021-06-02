package com.projectomega.main.packets.datatype;

import com.projectomega.main.packets.PacketUtil;

public class Position {

    private final int x;
    private final int y;
    private final int z;

    public Position(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public byte[] build() {
        byte[] bytes = new byte[8];
        long build = positionToLong(x, y, z);
        PacketUtil.writeLong(bytes, 0, build);
        for (int i = 0; i < 8; i++)
            System.out.println("==  " + bytes[i]);
        return bytes;
    }

    public static long positionToLong(int x, int y, int z) {
        return (((long) x & 0x3FFFFFF) << 38) | (((long) z & 0x3FFFFFF) << 12) | ((long) y & 0xFFF);
    }
}
