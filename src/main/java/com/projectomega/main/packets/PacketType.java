package com.projectomega.main.packets;

public enum PacketType {

    HANDSHAKE(0, PacketDirection.SERVERBOUND),
    STATUS_PING(0, PacketDirection.CLIENTBOUND),

    HANDSHAKE_PING(1,PacketDirection.SERVERBOUND),
    HANDSHAKE_PONG(1,PacketDirection.CLIENTBOUND),

    LOGIN_SUCCESS(2,PacketDirection.CLIENTBOUND),

    CLIENT_SETTINGS(0x05,PacketDirection.SERVERBOUND),
    TIME_UPDATE(0x4E,PacketDirection.CLIENTBOUND),

    KEEP_ALIVE_CLIENTBOUND_OLD(0x21 ,PacketDirection.CLIENTBOUND),
    KEEP_ALIVE_SERVERBOUND_OLD(0x0F,PacketDirection.SERVERBOUND),
    KEEP_ALIVE_CLIENTBOUND(0x1F ,PacketDirection.CLIENTBOUND),
    KEEP_ALIVE_SERVERBOUND(0x10,PacketDirection.SERVERBOUND),

    CHUNK_DATA(0x22,PacketDirection.CLIENTBOUND),

    PLAYER_POSITION_AND_LOOK(0x36 ,PacketDirection.CLIENTBOUND),
    RESPAWN(0x39 ,PacketDirection.CLIENTBOUND),
    JOIN_GAME_OLD(0x26 , PacketDirection.CLIENTBOUND ),
    JOIN_GAME(0x24 , PacketDirection.CLIENTBOUND ),

    CHAT_SERVERBOUND(0x03,PacketDirection.SERVERBOUND),
    CHAT_CLIENTBOUND(0x0F,PacketDirection.CLIENTBOUND),

    BOSS_BAR(0x0D,PacketDirection.CLIENTBOUND)

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
