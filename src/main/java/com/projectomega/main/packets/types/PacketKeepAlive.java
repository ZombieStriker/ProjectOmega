package com.projectomega.main.packets.types;

import com.projectomega.main.packets.PacketHandler;
import com.projectomega.main.packets.PacketType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketKeepAlive extends PacketHandler {
    public PacketKeepAlive() {
        super(PacketType.KEEP_ALIVE_CLIENTBOUND_OLD);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, ChannelHandlerContext ctx) {

    }
}
