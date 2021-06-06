package com.projectomega.main.packets.types;

import com.projectomega.main.packets.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

public class PacketPlayerPosition extends PacketHandler {
    public PacketPlayerPosition() {
        super(PacketType.PLAYER_POSITION);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, Channel ctx) {
        double x = bytebuf.readDouble();
        double y = bytebuf.readDouble();
        double z = bytebuf.readDouble();
        boolean onground = PacketUtil.readBoolean(bytebuf);
        InboundPacket packet = new InboundPacket(PacketType.PLAYER_POSITION,new Object[]{x,y,z,onground},ctx);
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.PLAYER_POSITION);
        if(packetlisteners!=null){
            for(PacketListener listener : packetlisteners){
                listener.onCall(packet);
            }
        }
    }
}
