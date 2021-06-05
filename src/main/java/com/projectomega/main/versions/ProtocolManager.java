package com.projectomega.main.versions;

import com.projectomega.main.game.Material;
import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.versions.handshake.ProtocolHandlerHandshake;
import com.projectomega.main.versions.v16.ProtocolHandler16;

public class ProtocolManager {

    public static ProtocolHandler handlerhandshake = new ProtocolHandlerHandshake();
    public static ProtocolHandler handler16 = new ProtocolHandler16();
    public static ProtocolHandler handler17 = new ProtocolHandler16();

    public static int getPacketIDForProtocol(int protocolVersion, PacketType type){
        if(protocolVersion<=0)
            return handlerhandshake.getPacketIDFromType(type);
        if(protocolVersion <= 754){
            return handler16.getPacketIDFromType(type);
        }else{
            return handler17.getPacketIDFromType(type);
        }
    }

    public static int getEntityIDForProtocol(int protocolVersion, EntityType data) {
        if(protocolVersion<=0)
            return -1;
        if(protocolVersion <= 754){
            return handler16.getEntityIDFromType(data);
        }else{
            return handler17.getEntityIDFromType(data);
        }
    }

    public static PacketType getPacketByID(int protocolversion, int packetid, boolean handshakePacket, PacketType.PacketDirection direction) {
        if(protocolversion<=0) {
            return handlerhandshake.getPacketTypeFromID(packetid, handshakePacket, direction);
        }
        if(protocolversion <= 754){
            return handler16.getPacketTypeFromID(packetid,handshakePacket,direction);
        }else{
            return handler17.getPacketTypeFromID(packetid,handshakePacket,direction);
        }

    }

    public static int getBlockIDByType(int protocolversion, Material type) {
        if(protocolversion<=0) {
            return handlerhandshake.getBlockIDByType(type);
        }
        if(protocolversion <= 754){
            return handler16.getBlockIDByType(type);
        }else{
            return handler17.getBlockIDByType(type);
        }
    }
}
