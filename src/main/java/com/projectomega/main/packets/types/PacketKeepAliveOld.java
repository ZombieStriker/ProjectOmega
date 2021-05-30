package com.projectomega.main.packets.types;

import com.projectomega.main.packets.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class PacketKeepAliveOld extends PacketHandler {
    public PacketKeepAliveOld() {
        super(PacketType.KEEP_ALIVE_CLIENTBOUND_OLD);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, ChannelHandlerContext ctx) {
        InboundPacket packet = new InboundPacket(PacketType.KEEP_ALIVE_CLIENTBOUND_OLD,new Object[]{},ctx.channel());
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.KEEP_ALIVE_CLIENTBOUND_OLD);
        if(packetlisteners!=null){
            for(PacketListener listener : packetlisteners){
                listener.onCall(packet);
            }
        }
    }
}
