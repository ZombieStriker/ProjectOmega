package com.projectomega.main.game.packetlogic;

import com.projectomega.main.packets.PacketManager;
import com.projectomega.main.packets.PacketType;

public class PacketLogicManager {

    public static void init(){
        PacketManager.registerPacketListener(PacketType.HANDSHAKE,new SendMOTDPacketLogic());
    }
}
