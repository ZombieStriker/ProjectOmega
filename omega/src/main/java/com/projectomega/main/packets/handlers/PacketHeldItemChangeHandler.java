package com.projectomega.main.packets.handlers;

import com.projectomega.main.packets.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

public class PacketHeldItemChangeHandler extends PacketHandler {
    public PacketHeldItemChangeHandler() {
        super(PacketType.HELD_ITEM_CHANGE_SERVERBOUND);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, Channel channel) {
        InboundPacket packet = new InboundPacket(PacketType.HELD_ITEM_CHANGE_SERVERBOUND,new Object[]{bytebuf.readShort()},channel);
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.HELD_ITEM_CHANGE_SERVERBOUND);
        if(packetlisteners!=null){
            for(PacketListener listener : packetlisteners){
                listener.onCall(packet);
            }
        }
    }
}
