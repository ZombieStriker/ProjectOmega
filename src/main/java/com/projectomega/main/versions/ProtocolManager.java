package com.projectomega.main.versions;

import com.projectomega.main.packets.PacketType;
import com.projectomega.main.versions.v16.ProtocolHandler16;

public class ProtocolManager {

    public static ProtocolHandler handler16 = new ProtocolHandler16();
    public static ProtocolHandler handler17 = new ProtocolHandler16();

    public static int getPacketIDForProtocol(int protocolVersion, PacketType type){
        if(protocolVersion <= 754){
            return handler16.getPacketIDFromType(type);
        }else{
            return handler17.getPacketIDFromType(type);
        }
    }
}
