package com.projectomega.main.packets;

import io.netty.buffer.ByteBuf;

import java.net.SocketAddress;

public abstract class PacketHandler {

    private PacketType type;

    public PacketHandler(PacketType type){
        this.type = type;
    }

    public abstract void call(ByteBuf byteBuf, int packetsize, SocketAddress socketAddress);

    public PacketType getPacketType() {
        return type;
    }
}
