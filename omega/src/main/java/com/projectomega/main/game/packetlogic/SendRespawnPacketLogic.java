package com.projectomega.main.game.packetlogic;

import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.*;
import com.projectomega.main.packets.datatype.UnsignedByte;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class SendRespawnPacketLogic implements PacketListener {
    @Override
    public void onCall(InboundPacket packet) {
        if(((int)packet.getData(0))==0){
            //Perform Respawn
            System.out.println("Respawn packet sent");
            OutboundPacket respawnPAcket = new OutboundPacket(PacketType.RESPAWN, 762,0,0L,new UnsignedByte((byte) 0),"default");
            Player player = Omega.getPlayerByChannel(packet.getChannel());
            player.sendPacket(respawnPAcket);
        }
    }
}
