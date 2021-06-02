package com.projectomega.main.versions.v16;

import com.projectomega.main.packets.PacketType;
import com.projectomega.main.versions.ProtocolHandler;

import java.util.HashMap;

public class ProtocolHandler16 extends ProtocolHandler {

    private HashMap<PacketType,Integer> packetIDs = new HashMap<>();

    public ProtocolHandler16(){
        packetIDs.put(PacketType.KEEP_ALIVE_SERVERBOUND,0x10);
        packetIDs.put(PacketType.HELD_ITEM_CHANGE_SERVERBOUND,0x25);
        packetIDs.put(PacketType.CLIENT_STATUS,0x04);
        packetIDs.put(PacketType.BLOCK_CHANGE,0x08);
        packetIDs.put(PacketType.CLICK_WINDOW,0x09);
        packetIDs.put(PacketType.CLIENT_SETTINGS,0x05);
        packetIDs.put(PacketType.CHAT_SERVERBOUND,0x03);
        packetIDs.put(PacketType.HANDSHAKE_PING,1);
        packetIDs.put(PacketType.BOSS_BAR,0x0C);
        packetIDs.put(PacketType.CHAT_CLIENTBOUND,0x0E);
        packetIDs.put(PacketType.CHUNK_DATA,0x20);
        packetIDs.put(PacketType.CLICK_WINDOW_BUTTON,0x08);
        packetIDs.put(PacketType.ENTITY_METADATA,0x44);
        packetIDs.put(PacketType.ENTITY_TELEPORT,0x56);
        packetIDs.put(PacketType.HANDSHAKE_PONG,1);
        packetIDs.put(PacketType.JOIN_GAME,0x24);
        packetIDs.put(PacketType.KEEP_ALIVE_CLIENTBOUND,0x1F);
        packetIDs.put(PacketType.LOGIN_SUCCESS,2);
        packetIDs.put(PacketType.OPEN_WINDOW,0x2D);
        packetIDs.put(PacketType.PLAYER_INFO,0x32);
        packetIDs.put(PacketType.PLAYER_POSITION_AND_LOOK,0x34);
        packetIDs.put(PacketType.RESPAWN,0x39);
        packetIDs.put(PacketType.SET_EXPERIENCE,0x48);
        packetIDs.put(PacketType.SOUND_EFFECT,0x51);
        packetIDs.put(PacketType.SPAWN_ENTITY,0x00);
        packetIDs.put(PacketType.SPAWN_LIVING_ENTITY,0x02);
        packetIDs.put(PacketType.STATUS_PING,0);
        packetIDs.put(PacketType.TIME_UPDATE,0x4E);
        packetIDs.put(PacketType.TITLE,0x4F);
        packetIDs.put(PacketType.UPDATE_HEALTH,0x49);
        packetIDs.put(PacketType.WINDOW_ITEMS,0x13);
    }

    public int getPacketIDFromType(PacketType type){
        return packetIDs.get(type);
    }
}
