package com.projectomega.main.packets.types;

import com.projectomega.main.packets.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

public class PacketPing extends PacketHandler {


    public PacketPing() {
        super(PacketType.HANDSHAKE_PING);
    }

    @Override
    public void call(ByteBuf byteBuf, int packetsize, Channel ctx) {
        long longdata = PacketUtil.readLong(byteBuf);
        InboundPacket packet = new InboundPacket(PacketType.HANDSHAKE_PING,new Object[]{longdata},ctx);
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.HANDSHAKE_PING);
        if(packetlisteners!=null){
            for(PacketListener listener : packetlisteners){
                listener.onCall(packet);
            }
        }
    }
}
