package com.projectomega.main.versions.v17;

import com.projectomega.main.packets.PacketType;
import com.projectomega.main.versions.ProtocolHandler;

import java.util.HashMap;

public class ProtocolHandler17 extends ProtocolHandler {
    private HashMap<PacketType,Integer> packetIDs = new HashMap<>();

    public int getPacketIDFromType(PacketType type){
        return packetIDs.get(type);
    }
}
