package com.projectomega.main.packets.types;

import com.projectomega.main.packets.PacketHandler;
import com.projectomega.main.packets.PacketType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketClickWindowButton extends PacketHandler {
    public PacketClickWindowButton() {
        super(PacketType.CLICK_WINDOW_BUTTON);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, ChannelHandlerContext ctx) {
        byte windowid = bytebuf.readByte();

    }
}
