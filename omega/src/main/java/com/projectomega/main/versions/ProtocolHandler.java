package com.projectomega.main.versions;

import com.projectomega.main.game.Material;
import com.projectomega.main.game.block.BlockData;
import com.projectomega.main.game.block.BlockDataTag;
import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.packets.PacketType;

import java.util.*;

public abstract class ProtocolHandler {

    protected List<PacketType> handshakePackets = new ArrayList<>();
    protected HashMap<PacketType, Integer> packetIDs = new HashMap<>();
    protected HashMap<EntityType, Integer> entityIDs = new HashMap<>();
    protected Material[] materialIDs = new Material[Material.values().length];
    protected LinkedList<BlockData> blockIDs = new LinkedList<>();

    public int getPacketIDFromType(PacketType type) {
        if (packetIDs.containsKey(type))
            return packetIDs.get(type);
        System.out.println("Failed to find packet type " + type.name() + "  || " + this.getClass().getName());
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

    public void registerBlockData(Material material, BlockDataTag... tags){
        int currentID = 0;
        for(BlockData data : blockIDs){
            currentID+=data.getDataValues();
        }
        BlockData data = new BlockData(currentID, material, tags);
        blockIDs.add(data);
    }


    public Integer getBlockIDByType(Material type) {
        int id = 0;
        for (BlockData data: blockIDs) {
            if(data.getMaterial()==type) {
                return id;
            }
            id+= data.getDataValues();
        }
        return -1;
    }

    public Integer getItemIDFromType(Material type) {
        for (int i = 0; i < materialIDs.length; i++) {
            if (materialIDs[i] == type)
                return i;
        }
        return -1;
    }


}
