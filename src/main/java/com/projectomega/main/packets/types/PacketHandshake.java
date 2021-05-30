package com.projectomega.main.packets.types;

import com.projectomega.main.game.Omega;
import com.projectomega.main.packets.*;
import com.projectomega.main.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

public class PacketHandshake extends PacketHandler {


    public PacketHandshake() {
        super(PacketType.HANDSHAKE);
    }

    @Override
    public void call(ByteBuf byteBuf, int packetsize, ChannelHandlerContext ctx) {
        if(Omega.getPlayerByChannel(ctx.channel())==null) {
            int protocolVersion = PacketUtil.readVarInt(byteBuf);
            String ip = ByteUtils.buildString(byteBuf);
            int port = byteBuf.readShort();
            int status = byteBuf.readByte();

            InboundPacket packet = new InboundPacket(PacketType.HANDSHAKE, new Object[]{protocolVersion, ip, port, status}, ctx.channel());
            List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.HANDSHAKE);
            if (packetlisteners != null) {
                for (PacketListener listener : packetlisteners) {
                    listener.onCall(packet);
                }
            }
        }
    }
}
