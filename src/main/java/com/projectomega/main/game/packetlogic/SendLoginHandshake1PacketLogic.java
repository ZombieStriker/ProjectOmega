package com.projectomega.main.game.packetlogic;

import com.projectomega.main.packets.*;
import com.projectomega.main.packets.datatype.UnsignedByte;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.utils.NBTTagUtil;
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


            // OutboundPacket keepalive = new OutboundPacket(PacketType.KEEP_ALIVE_CLIENTBOUND,new Object[]{System.currentTimeMillis()});
           // PacketUtil.writePacketToOutputStream(packet.getChannel(),keepalive);

           // OutboundPacket timeTick = new OutboundPacket(PacketType.TIME_UPDATE,new Object[]{10L,10L});
           // PacketUtil.writePacketToOutputStream(packet.getChannel(),timeTick);

           // OutboundPacket joingame = new OutboundPacket(PacketType.JOIN_GAME, new Object[]{1,true, new UnsignedByte((byte) 0),(byte)-1,new VarInt(1),"world","world_nether","world_the_end", NBTTagUtil.generateDimensionCodec(), NBTTagUtil.generateDimensionType(),"world",0l,new VarInt(32),new VarInt(10),false,true,false,false});
            OutboundPacket joingame = new OutboundPacket(PacketType.JOIN_GAME, new Object[]{1,true, new UnsignedByte((byte) 0),(byte)-1,new VarInt(1),"overworld", NBTTagUtil.generateDimensionCodec(), NBTTagUtil.generateDimensionType(),"overworld",0l,new VarInt(32),new VarInt(10),false,true,false,false});
            PacketUtil.writePacketToOutputStream(packet.getChannel(),joingame);

           // OutboundPacket repsawn = new OutboundPacket(PacketType.RESPAWN, new Object[]{NBTTagUtil.generateDimensionTypeRegistryEntry("overworldandstuff",0),"overworld",0L,new UnsignedByte((byte) 0), new UnsignedByte((byte) 0),false,false,false});
           // PacketUtil.writePacketToOutputStream(packet.getChannel(),repsawn);


        }
    }
}
