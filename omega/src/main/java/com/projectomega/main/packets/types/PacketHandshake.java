package com.projectomega.main.packets.types;

import com.projectomega.main.game.Omega;
import com.projectomega.main.packets.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.util.List;

public class PacketHandshake extends PacketHandler {


    public PacketHandshake() {
        super(PacketType.HANDSHAKE);
    }

    @Override
    public void call(ByteBuf byteBuf, int packetsize, Channel ctx) {
        if (Omega.getPlayerByChannel(ctx) == null) {
            int protocolVersion = PacketUtil.readVarInt(byteBuf);
            String ip = PacketUtil.buildString(byteBuf);
            int port = byteBuf.readShort();
            int status = byteBuf.readByte();

            //TODO: This should be a seperate packet, so lets see if this works
            String s = PacketUtil.buildString(byteBuf);
            if(s.length() > 2)
            s= s.substring(2);

            InboundPacket packet = new InboundPacket(PacketType.HANDSHAKE, new Object[]{protocolVersion, ip, port, status, s}, ctx);
            List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.HANDSHAKE);
            if (packetlisteners != null) {
                for (PacketListener listener : packetlisteners) {
                    listener.onCall(packet);
                }
            }
        }
    }
}
