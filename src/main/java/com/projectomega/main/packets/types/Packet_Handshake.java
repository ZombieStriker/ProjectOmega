package com.projectomega.main.packets.types;

import com.projectomega.main.packets.*;
import com.projectomega.main.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.net.SocketAddress;
import java.util.List;

public class Packet_Handshake extends PacketHandler {


    public Packet_Handshake() {
        super(PacketType.HANDSHAKE);
    }

    @Override
    public void call(ByteBuf byteBuf, int packetsize, SocketAddress socketAddress, Channel channel) {
        int int1 = byteBuf.readByte();
        int int2 = byteBuf.readByte();
        int messageLength = byteBuf.readByte();
        String ip = ByteUtils.buildString(byteBuf,messageLength);
        int port =  byteBuf.readShort();
        int status = byteBuf.readByte();
        int k = byteBuf.readByte();
        int l = byteBuf.readByte();
        InboundPacket packet = new InboundPacket(PacketType.HANDSHAKE,new Object[]{ip,port,status},socketAddress);
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.HANDSHAKE);
        if(packetlisteners!=null){
            for(PacketListener listener : packetlisteners){
                listener.onCall(packet);
            }
        }
    }
}
