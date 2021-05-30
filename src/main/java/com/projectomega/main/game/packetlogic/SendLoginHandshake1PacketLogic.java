package com.projectomega.main.game.packetlogic;

import com.projectomega.main.events.EventManager;
import com.projectomega.main.events.types.PlayerJoinEvent;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.*;
import com.projectomega.main.packets.datatype.UnsignedByte;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.utils.NBTTagUtil;
import me.nullicorn.nedit.type.NBTCompound;

import java.util.UUID;

public class SendLoginHandshake1PacketLogic extends PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        if (((int) packet.getData(3)) == 2) {
            //OutboundPacket outboundPacket = new OutboundPacket(PacketType.LOGIN_SUCCESS,new Object[]{UUID.randomUUID(),"Test"});
            OutboundPacket outboundPacket = new OutboundPacket(PacketType.LOGIN_SUCCESS, new Object[]{UUID.randomUUID().toString(), "Test"});
            PacketUtil.writePacketToOutputStream(packet.getChannel(), outboundPacket);
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            NBTCompound dimensionCodec = NBTTagUtil.generateDimensionCodec();

            // OutboundPacket joingame = new OutboundPacket(PacketType.JOIN_GAME, new Object[]{1,true, new UnsignedByte((byte) 0),(byte)-1,new VarInt(1),"world","world_nether","world_the_end", NBTTagUtil.generateDimensionCodec(), NBTTagUtil.generateDimensionType(),"world",0l,new VarInt(32),new VarInt(10),false,true,false,false});
            //OutboundPacket joingame = new OutboundPacket(PacketType.JOIN_GAME, new Object[]{1,true, new UnsignedByte((byte) 0),(byte)-1,new VarInt(1),"overworld", dimensionCodec, NBTTagUtil.generateDimensionType(),"overworld",0l,new VarInt(32),new VarInt(10),false,true,false,false});

            Player player = new Player(packet.getChannel(), (Integer) packet.getData(0));

            PlayerJoinEvent joinEvent = new PlayerJoinEvent(player);
            EventManager.call(joinEvent);
            if (!joinEvent.isCanceled()) {
                if(player.getProtocolVersion() < 750) {
                    //Outdated for 1.13-1.15
                    OutboundPacket joingame = new OutboundPacket(PacketType.JOIN_GAME_OLD, new Object[]{0, new UnsignedByte((byte) 0), 0, 0l, new UnsignedByte((byte) 32), "flat", new VarInt(2), false, true});
                    PacketUtil.writePacketToOutputStream(packet.getChannel(), joingame);

                    Omega.addPlayerConnection(player);

                    player.sendPacket(new OutboundPacket(PacketType.PLAYER_POSITION_AND_LOOK, new Object[]{0d, 0d, 0d, 0f, 0f, (byte) 0, new VarInt(1)}));
                }else{
                    OutboundPacket joingame = new OutboundPacket(PacketType.JOIN_GAME, new Object[]{1,true, new UnsignedByte((byte) 0),(byte)-1,new VarInt(1),"overworld", dimensionCodec, NBTTagUtil.generateDimensionType(),"overworld",0l,new VarInt(32),new VarInt(10),false,true,false,false});
                    PacketUtil.writePacketToOutputStream(packet.getChannel(), joingame);

                    Omega.addPlayerConnection(player);

                }
            }


            // OutboundPacket repsawn = new OutboundPacket(PacketType.RESPAWN, new Object[]{NBTTagUtil.generateDimensionTypeRegistryEntry("overworldandstuff",0),"overworld",0L,new UnsignedByte((byte) 0), new UnsignedByte((byte) 0),false,false,false});
            // PacketUtil.writePacketToOutputStream(packet.getChannel(),repsawn);


        }
    }
}
