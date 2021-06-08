package com.projectomega.main.packets.handlers;

import com.projectomega.main.packets.PacketHandler;
import com.projectomega.main.packets.PacketType;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

public class PacketClickWindowButtonHandler extends PacketHandler {
    public PacketClickWindowButtonHandler() {
        super(PacketType.CLICK_WINDOW_BUTTON);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, Channel ctx) {
        byte windowid = bytebuf.readByte();

    }
}
