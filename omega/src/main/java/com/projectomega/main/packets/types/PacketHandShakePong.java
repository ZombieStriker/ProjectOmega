package com.projectomega.main.packets.types;

import com.projectomega.main.packets.OutboundPacket;
import com.projectomega.main.packets.PacketType;

public class PacketHandShakePong extends OutboundPacket {
    public PacketHandShakePong(int protcolVersion, Object data) {
        super(PacketType.HANDSHAKE_PONG, protcolVersion, data);
    }
}
