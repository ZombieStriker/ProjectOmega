package com.projectomega.main.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.SocketAddress;

public abstract class PacketHandler {

    private PacketType type;

    public PacketHandler(PacketType type){
        this.type = type;
    }


    public PacketType getPacketType() {
        return type;
    }

    public abstract void call(ByteBuf bytebuf, int i, ChannelHandlerContext ctx);
}
