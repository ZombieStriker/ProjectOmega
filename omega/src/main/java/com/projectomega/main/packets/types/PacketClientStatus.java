package com.projectomega.main.packets.types;

import com.projectomega.main.packets.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

public class PacketClientStatus extends PacketHandler {
    public PacketClientStatus() {
        super(PacketType.CLIENT_STATUS);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, Channel ctx) {
        int status = PacketUtil.readVarInt(bytebuf);
        InboundPacket packet = new InboundPacket(PacketType.CLIENT_STATUS,new Object[]{status},ctx);
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.CLIENT_STATUS);
        if(packetlisteners!=null){
            for(PacketListener listener : packetlisteners){
                listener.onCall(packet);
            }
        }
    }
}
