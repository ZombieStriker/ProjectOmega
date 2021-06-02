package com.projectomega.main.versions.handshake;

import com.projectomega.main.packets.PacketType;
import com.projectomega.main.versions.ProtocolHandler;

public class ProtocolHandlerHandshake extends ProtocolHandler {

    public ProtocolHandlerHandshake(){
        packetIDs.put(PacketType.HANDSHAKE,0);
        packetIDs.put(PacketType.STATUS_PING,0);
        packetIDs.put(PacketType.HANDSHAKE_PONG,0x01);
        packetIDs.put(PacketType.HANDSHAKE_PING,0x01);
        packetIDs.put(PacketType.LOGIN_SUCCESS,0x02);

        handshakePackets.add(PacketType.STATUS_PING);
        handshakePackets.add(PacketType.HANDSHAKE);
        handshakePackets.add(PacketType.HANDSHAKE_PONG);
        handshakePackets.add(PacketType.HANDSHAKE_PING);
        handshakePackets.add(PacketType.LOGIN_SUCCESS);
    }
}
