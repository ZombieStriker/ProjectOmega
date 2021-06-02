package com.projectomega.main.versions;

import com.projectomega.main.packets.PacketType;

public abstract class ProtocolHandler {

    public abstract int getPacketIDFromType(PacketType type);
}
