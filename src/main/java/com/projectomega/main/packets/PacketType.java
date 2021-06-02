package com.projectomega.main.packets;

public enum PacketType {

    HANDSHAKE(0, PacketDirection.SERVERBOUND),
    STATUS_PING(0, PacketDirection.CLIENTBOUND),

    HANDSHAKE_PING(1,PacketDirection.SERVERBOUND),
    HANDSHAKE_PONG(1,PacketDirection.CLIENTBOUND),

    LOGIN_SUCCESS(2,PacketDirection.CLIENTBOUND),

    SPAWN_ENTITY(0x00,PacketDirection.CLIENTBOUND),

    CLIENT_SETTINGS(0x05,PacketDirection.SERVERBOUND),
    TIME_UPDATE(0x4E,PacketDirection.CLIENTBOUND),

    ENTITY_METADATA(0x44,PacketDirection.CLIENTBOUND),

    KEEP_ALIVE_CLIENTBOUND_OLD(0x21 ,PacketDirection.CLIENTBOUND),
    KEEP_ALIVE_SERVERBOUND_OLD(0x0F,PacketDirection.SERVERBOUND),
    KEEP_ALIVE_CLIENTBOUND(0x1F ,PacketDirection.CLIENTBOUND),
    KEEP_ALIVE_SERVERBOUND(0x10,PacketDirection.SERVERBOUND),

    CLICK_WINDOW_BUTTON(0x08,PacketDirection.SERVERBOUND),
    CLICK_WINDOW(0x09,PacketDirection.SERVERBOUND),

    SOUND_EFFECT(0x52, PacketDirection.CLIENTBOUND),
    TITLE(0x50,PacketDirection.CLIENTBOUND),

    PLAYER_INFO(0x32,PacketDirection.CLIENTBOUND),

    HELD_ITEM_CHANGE(0x40,PacketDirection.CLIENTBOUND),
    SET_EXPERIENCE(0x48,PacketDirection.CLIENTBOUND),

    CHUNK_DATA(0x22,PacketDirection.CLIENTBOUND),
    WINDOW_ITEMS(0x13,PacketDirection.CLIENTBOUND),
    OPEN_WINDOW(0x2D,PacketDirection.CLIENTBOUND),

    PLAYER_POSITION_AND_LOOK(0x36 ,PacketDirection.CLIENTBOUND),
    RESPAWN(0x3B ,PacketDirection.CLIENTBOUND),
    JOIN_GAME_OLD(0x26 , PacketDirection.CLIENTBOUND ),
    JOIN_GAME(0x24 , PacketDirection.CLIENTBOUND ),

    HELD_ITEM_CHANGE_SERVERBOUND(0x23,PacketDirection.SERVERBOUND),

    CHAT_SERVERBOUND(0x03,PacketDirection.SERVERBOUND),
    CHAT_CLIENTBOUND(0x0F,PacketDirection.CLIENTBOUND),

    BOSS_BAR(0x0D,PacketDirection.CLIENTBOUND),
    ENTITY_TELEPORT(0x56,PacketDirection.CLIENTBOUND),
    UPDATE_HEALTH(0x49,PacketDirection.CLIENTBOUND ),
    CLIENT_STATUS(0x04,PacketDirection.SERVERBOUND),
    SPAWN_LIVING_ENTITY(0x03,PacketDirection.CLIENTBOUND);

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
