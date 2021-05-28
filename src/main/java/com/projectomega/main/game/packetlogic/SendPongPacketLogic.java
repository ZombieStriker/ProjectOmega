package com.projectomega.main.game.packetlogic;

import com.projectomega.main.packets.*;

public class SendPongPacketLogic extends PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        OutboundPacket outboundpacket = new OutboundPacket(PacketType.HANDSHAKE_PONG,new Object[]{packet.getData(0)});
        System.out.println(packet.getData(0));
        PacketUtil.writePacketToOutputStream(packet.getChannel(),outboundpacket);
    }
}
