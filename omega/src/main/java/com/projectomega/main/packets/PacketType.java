package com.projectomega.main.packets;

public enum PacketType {

    //Handshake
    HANDSHAKE(PacketDirection.SERVERBOUND),
    HANDSHAKE_PING(PacketDirection.SERVERBOUND),


    //Login packets
    LOGIN_START(PacketDirection.SERVERBOUND),


    //Play serverbound
    CHAT_SERVERBOUND(PacketDirection.SERVERBOUND),
    CLIENT_STATUS(PacketDirection.SERVERBOUND),
    CLIENT_SETTINGS(PacketDirection.SERVERBOUND),
    CLICK_WINDOW_BUTTON(PacketDirection.SERVERBOUND),
    CLICK_WINDOW(PacketDirection.SERVERBOUND),
    KEEP_ALIVE_SERVERBOUND(PacketDirection.SERVERBOUND),
    HELD_ITEM_CHANGE_SERVERBOUND(PacketDirection.SERVERBOUND),
    PLAYER_POSITION(PacketDirection.SERVERBOUND),
    PLAYER_POSITION_AND_ROTATION(PacketDirection.SERVERBOUND),

    //Handshake
    STATUS_PING( PacketDirection.CLIENTBOUND),
    HANDSHAKE_PONG(PacketDirection.CLIENTBOUND),
    LOGIN_SUCCESS(PacketDirection.CLIENTBOUND),

    //Play packets
    SPAWN_ENTITY(PacketDirection.CLIENTBOUND),
    SPAWN_LIVING_ENTITY(PacketDirection.CLIENTBOUND),
    BLOCK_CHANGE(PacketDirection.CLIENTBOUND),
    BOSS_BAR(PacketDirection.CLIENTBOUND),
    CHAT_CLIENTBOUND(PacketDirection.CLIENTBOUND),
    WINDOW_ITEMS(PacketDirection.CLIENTBOUND),
    KEEP_ALIVE_CLIENTBOUND(PacketDirection.CLIENTBOUND),
    CHUNK_DATA(PacketDirection.CLIENTBOUND),
    JOIN_GAME( PacketDirection.CLIENTBOUND ),
    OPEN_WINDOW(PacketDirection.CLIENTBOUND),
    PLAYER_INFO(PacketDirection.CLIENTBOUND),
    PLAYER_POSITION_AND_LOOK(PacketDirection.CLIENTBOUND),
    RESPAWN(PacketDirection.CLIENTBOUND),
    HELD_ITEM_CHANGE(PacketDirection.CLIENTBOUND),
    ENTITY_METADATA(PacketDirection.CLIENTBOUND),
    SET_EXPERIENCE(PacketDirection.CLIENTBOUND),
    UPDATE_HEALTH(PacketDirection.CLIENTBOUND ),
    TIME_UPDATE(PacketDirection.CLIENTBOUND),
    TITLE(PacketDirection.CLIENTBOUND),
    SOUND_EFFECT(PacketDirection.CLIENTBOUND),
    ENTITY_TELEPORT(PacketDirection.CLIENTBOUND),
    MULTI_BLOCK_CHANGE(PacketDirection.CLIENTBOUND),
    UPDATE_VIEW_POSITION(PacketDirection.CLIENTBOUND),
    UPDATE_VIEW_DISTANCE(PacketDirection.CLIENTBOUND),
    SET_SLOT(PacketDirection.CLIENTBOUND),
    DISCONNECT(PacketDirection.CLIENTBOUND),
    UNLOAD_CHUNK(PacketDirection.CLIENTBOUND),
    SET_TITLE_TEXT(PacketDirection.CLIENTBOUND),
    SET_TITLE_SUBTITLE(PacketDirection.CLIENTBOUND);

    private PacketDirection direction;

    PacketType(PacketDirection dir){
        this.direction = dir;
    }


    public PacketDirection getDirection(){
        return direction;
    }

    public enum PacketDirection {
        SERVERBOUND,CLIENTBOUND;
    }
}
