package com.projectomega.main.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.net.SocketAddress;

public abstract class PacketHandler {

    private PacketType type;

    public PacketHandler(PacketType type){
        this.type = type;
    }

    public abstract void call(ByteBuf byteBuf, int packetsize, SocketAddress socketAddress, Channel channel);

    public PacketType getPacketType() {
        return type;
    }
}
