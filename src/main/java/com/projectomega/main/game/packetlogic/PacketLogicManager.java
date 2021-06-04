package com.projectomega.main.game.packetlogic;

import com.projectomega.main.packets.PacketManager;
import com.projectomega.main.packets.PacketType;

public class PacketLogicManager {

    public static void init(){
        PacketManager.registerPacketListener(PacketType.HANDSHAKE,new SendMOTDPacketLogic());
        PacketManager.registerPacketListener(PacketType.HANDSHAKE_PING,new SendPongPacketLogic());
        PacketManager.registerPacketListener(PacketType.HANDSHAKE,new SendLoginHandshake1PacketLogic());
        PacketManager.registerPacketListener(PacketType.CLIENT_SETTINGS,new SendClientSettingsPacketLogic());
        PacketManager.registerPacketListener(PacketType.CLIENT_STATUS,new SendRespawnPacketLogic());
        PacketManager.registerPacketListener(PacketType.HELD_ITEM_CHANGE_SERVERBOUND,new UpdateHeldItemSlot());
        PacketManager.registerPacketListener(PacketType.PLAYER_POSITION,new UpdatePlayerPositionPacketLogic());
        PacketManager.registerPacketListener(PacketType.PLAYER_POSITION_AND_ROTATION,new UpdatePlayerPositionAndRotationPacketLogic());
    }
}
