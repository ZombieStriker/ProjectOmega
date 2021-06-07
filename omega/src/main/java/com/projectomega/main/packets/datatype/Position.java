package com.projectomega.main.packets.datatype;

import com.projectomega.main.packets.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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
        ByteBuf byteBuf = Unpooled.buffer();
        long build = positionToLong(x, y, z);
        PacketUtil.writeLong(byteBuf, 0, build);
        return byteBuf.array();
    }

    public static long positionToLong(int x, int y, int z) {
        return ((((long) x) & 0x3FFFFFF) << 38) | ((((long) z) & 0x3FFFFFF) << 12) | (((long) y) & 0xFFF);
    }
}
