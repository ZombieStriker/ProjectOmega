package com.projectomega.main.packets;

public enum PacketType {

    //Play handshake
    HANDSHAKE(0, PacketDirection.SERVERBOUND),
    HANDSHAKE_PING(1,PacketDirection.SERVERBOUND),

    //Play serverbound
    CHAT_SERVERBOUND(0x03,PacketDirection.SERVERBOUND),
    CLIENT_STATUS(0x04,PacketDirection.SERVERBOUND),
    CLIENT_SETTINGS(0x05,PacketDirection.SERVERBOUND),
    CLICK_WINDOW_BUTTON(0x08,PacketDirection.SERVERBOUND),
    CLICK_WINDOW(0x09,PacketDirection.SERVERBOUND),
    KEEP_ALIVE_SERVERBOUND(0x10,PacketDirection.SERVERBOUND),
    HELD_ITEM_CHANGE_SERVERBOUND(0x25,PacketDirection.SERVERBOUND),

    //Handshake
    STATUS_PING(0, PacketDirection.CLIENTBOUND),
    HANDSHAKE_PONG(1,PacketDirection.CLIENTBOUND),
    LOGIN_SUCCESS(2,PacketDirection.CLIENTBOUND),

    //Play packets
    SPAWN_ENTITY(0x00,PacketDirection.CLIENTBOUND),
    SPAWN_LIVING_ENTITY(0x02,PacketDirection.CLIENTBOUND),
    BLOCK_CHANGE(0x0B, PacketDirection.CLIENTBOUND),
    BOSS_BAR(0x0C,PacketDirection.CLIENTBOUND),
    CHAT_CLIENTBOUND(0x0E,PacketDirection.CLIENTBOUND),
    WINDOW_ITEMS(0x13,PacketDirection.CLIENTBOUND),
    KEEP_ALIVE_CLIENTBOUND(0x1F ,PacketDirection.CLIENTBOUND),
    CHUNK_DATA(0x20,PacketDirection.CLIENTBOUND),
    JOIN_GAME(0x24 , PacketDirection.CLIENTBOUND ),
    OPEN_WINDOW(0x2D,PacketDirection.CLIENTBOUND),
    PLAYER_INFO(0x32,PacketDirection.CLIENTBOUND),
    PLAYER_POSITION_AND_LOOK(0x34 ,PacketDirection.CLIENTBOUND),
    RESPAWN(0x39 ,PacketDirection.CLIENTBOUND),
    HELD_ITEM_CHANGE(0x3F,PacketDirection.CLIENTBOUND),
    ENTITY_METADATA(0x44,PacketDirection.CLIENTBOUND),
    SET_EXPERIENCE(0x48,PacketDirection.CLIENTBOUND),
    UPDATE_HEALTH(0x49,PacketDirection.CLIENTBOUND ),
    TIME_UPDATE(0x4E,PacketDirection.CLIENTBOUND),
    TITLE(0x4F,PacketDirection.CLIENTBOUND),
    SOUND_EFFECT(0x51, PacketDirection.CLIENTBOUND),
    ENTITY_TELEPORT(0x56,PacketDirection.CLIENTBOUND),
    ;

    private int id;
    private PacketDirection direction;

    PacketType(int type, PacketDirection dir){
        this.id= type;
        this.direction = dir;
    }

    public int getId(){
        return id;
    }

    public PacketDirection getDirection(){
        return direction;
    }

    enum PacketDirection {
        SERVERBOUND,CLIENTBOUND;
    }
}
