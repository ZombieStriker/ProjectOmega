package com.projectomega.main.game.packetlogic;

import com.projectomega.main.packets.*;

public class SendLoginHandshake1PacketLogic extends PacketListener {

    @Override
    public void onCall(InboundPacket packet) {
        System.out.println(packet.getChannel()+"|"+packet.getData(0)+"|"+packet.getData(1)+"|"+packet.getData(2));
        if(((int)packet.getData(2)) ==2) {

        }
    }
}
