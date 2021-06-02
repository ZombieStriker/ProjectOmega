package com.projectomega.main.versions;

import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.packets.PacketType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ProtocolHandler {

    protected List<PacketType> handshakePackets = new ArrayList<>();
    protected HashMap<PacketType, Integer> packetIDs = new HashMap<>();
    protected HashMap<EntityType, Integer> entityIDs = new HashMap<>();

    public int getPacketIDFromType(PacketType type) {
        if (packetIDs.containsKey(type))
            return packetIDs.get(type);
        System.out.println("Failed to find packet type "+type.name()+"  || "+this.getClass().getName());
        return -1;
    }

    public int getEntityIDFromType(EntityType type) {
        if (entityIDs.containsKey(type))
            return entityIDs.get(type);
        if (type.isLiving()) {
            return entityIDs.get(EntityType.PIG);
        } else {
            return entityIDs.get(EntityType.ARROW);
        }
    }

    public PacketType getPacketTypeFromID(int packetid, boolean handshakePacket, PacketType.PacketDirection direction) {
        if (handshakePacket) {
            for (PacketType type : handshakePackets) {
                if (type.getDirection() == direction)
                    if (getPacketIDFromType(type) == packetid)
                        return type;
            }
        } else {
            for (Map.Entry<PacketType, Integer> type : packetIDs.entrySet()) {
                if (handshakePackets.contains(type.getKey()))
                    continue;
                if (type.getKey().getDirection() == direction)
                    if (type.getValue() == packetid)
                        return type.getKey();
            }
        }
        return null;
    }
}
