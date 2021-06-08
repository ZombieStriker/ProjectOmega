package com.projectomega.main.packets.handlers;

import com.projectomega.main.packets.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

public class PacketPlayerPositionAndRotationHandler extends PacketHandler {
    public PacketPlayerPositionAndRotationHandler() {
        super(PacketType.PLAYER_POSITION_AND_ROTATION);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, Channel ctx) {
        double x = bytebuf.readDouble();
        double y = bytebuf.readDouble();
        double z = bytebuf.readDouble();
        float yaw = bytebuf.readFloat();
        float pitch = bytebuf.readFloat();
        boolean onground = PacketUtil.readBoolean(bytebuf);
        InboundPacket packet = new InboundPacket(PacketType.PLAYER_POSITION_AND_ROTATION,new Object[]{x,y,z,yaw,pitch,onground},ctx);
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.PLAYER_POSITION_AND_ROTATION);
        if(packetlisteners!=null){
            for(PacketListener listener : packetlisteners){
                listener.onCall(packet);
            }
        }

    }
}
