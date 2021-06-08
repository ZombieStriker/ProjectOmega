package com.projectomega.main.packets.types;

import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;

public class PacketStatusPing extends OutboundPacket {

    public PacketStatusPing(int protocolVersion, String message) {
        super(PacketType.STATUS_PING,protocolVersion,message);
    }
}
