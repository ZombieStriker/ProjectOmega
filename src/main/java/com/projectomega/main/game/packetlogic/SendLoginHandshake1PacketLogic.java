package com.projectomega.main.game.packetlogic;

import com.projectomega.main.packets.*;
import com.projectomega.main.packets.datatype.UnsignedByte;
import me.nullicorn.nedit.NBTWriter;

import java.util.UUID;

public class SendLoginHandshake1PacketLogic extends PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        if(((int)packet.getData(2)) ==2) {
            OutboundPacket outboundPacket = new OutboundPacket(PacketType.LOGIN_SUCCESS,new Object[]{UUID.randomUUID(),"Test"});
            PacketUtil.writePacketToOutputStream(packet.getChannel(),outboundPacket);
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            OutboundPacket keepalive = new OutboundPacket(PacketType.KEEP_ALIVE_CLIENTBOUND,new Object[]{System.currentTimeMillis()});
            PacketUtil.writePacketToOutputStream(packet.getChannel(),keepalive);
            OutboundPacket timeTick = new OutboundPacket(PacketType.TIME_UPDATE,new Object[]{10L,10L});
            PacketUtil.writePacketToOutputStream(packet.getChannel(),timeTick);

            OutboundPacket respawn = new OutboundPacket(PacketType.JOIN_GAME, new Object[]{1,true, new UnsignedByte((byte) 0),(byte)0});
            PacketUtil.writePacketToOutputStream(packet.getChannel(),respawn);

        }
    }
}
