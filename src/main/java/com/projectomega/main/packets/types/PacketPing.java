package com.projectomega.main.packets.types;

import com.projectomega.main.packets.*;
import com.projectomega.main.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public class PacketPing extends PacketHandler {


    public PacketPing() {
        super(PacketType.HANDSHAKE_PING);
    }

    @Override
    public void call(ByteBuf byteBuf, int packetsize, ChannelHandlerContext ctx) {
       /* int int1 = byteBuf.readByte();
        int int2 = byteBuf.readByte();
        int messageLength = byteBuf.readByte();
        String ip = ByteUtils.buildString(byteBuf,messageLength);
        int port =  byteBuf.readShort();
        int status = byteBuf.readByte();
        int k = byteBuf.readByte();
        int l = byteBuf.readByte();*/

        long longdata = byteBuf.readLong();

        InboundPacket packet = new InboundPacket(PacketType.HANDSHAKE_PING,new Object[]{longdata},ctx.channel());
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.HANDSHAKE_PING);
        if(packetlisteners!=null){
            for(PacketListener listener : packetlisteners){
                listener.onCall(packet);
            }
        }
    }
}
