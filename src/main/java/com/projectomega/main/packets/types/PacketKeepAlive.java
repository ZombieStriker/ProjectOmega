package com.projectomega.main.packets.types;

import com.projectomega.main.packets.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

public class PacketKeepAlive extends PacketHandler {
    public PacketKeepAlive() {
        super(PacketType.KEEP_ALIVE_CLIENTBOUND);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, Channel ctx) {
        InboundPacket packet = new InboundPacket(PacketType.KEEP_ALIVE_CLIENTBOUND,new Object[]{},ctx);
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.KEEP_ALIVE_CLIENTBOUND);
        if(packetlisteners!=null){
            for(PacketListener listener : packetlisteners){
                listener.onCall(packet);
            }
        }
    }
}
