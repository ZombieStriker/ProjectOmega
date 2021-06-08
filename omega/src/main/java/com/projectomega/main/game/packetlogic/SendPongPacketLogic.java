package com.projectomega.main.game.packetlogic;

import com.projectomega.main.packets.*;
import com.projectomega.main.packets.types.PacketHandShakePong;

public class SendPongPacketLogic implements PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        OutboundPacket outboundpacket = new PacketHandShakePong(754,packet.getData(0));
        PacketUtil.writePacketToOutputStream(packet.getChannel(),outboundpacket);
    }
}
