package com.projectomega.main.packets.types;

import com.projectomega.main.packets.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class PacketHeldItemChange extends PacketHandler {
    public PacketHeldItemChange() {
        super(PacketType.HELD_ITEM_CHANGE_SERVERBOUND);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, ChannelHandlerContext ctx) {
        InboundPacket packet = new InboundPacket(PacketType.HELD_ITEM_CHANGE_SERVERBOUND,new Object[]{bytebuf.readShort()},ctx.channel());
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.HELD_ITEM_CHANGE_SERVERBOUND);
        if(packetlisteners!=null){
            for(PacketListener listener : packetlisteners){
                listener.onCall(packet);
            }
        }
    }
}
