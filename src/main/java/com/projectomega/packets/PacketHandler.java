package com.projectomega.packets;

import java.io.InputStream;

public abstract class PacketHandler {

    private PacketType type;

    public PacketHandler(PacketType type){
        this.type = type;
    }

    public abstract void call(InputStream stream, int packetsize);

    public PacketType getPacketType() {
        return type;
    }
}
