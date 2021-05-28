package com.projectomega.main.packets;

public enum PacketType {

    HANDSHAKE(0, PacketDirection.SERVERBOUND),
    STATUS_PING(0, PacketDirection.CLIENTBOUND),

    HANDSHAKE_PING(1,PacketDirection.SERVERBOUND),
    HANDSHAKE_PONG(1,PacketDirection.CLIENTBOUND),

    LOGIN_SUCCESS(2,PacketDirection.CLIENTBOUND),

    TIME_UPDATE(0x4E,PacketDirection.CLIENTBOUND),

    KEEP_ALIVE_CLIENTBOUND(0x1F ,PacketDirection.CLIENTBOUND),
    KEEP_ALIVE_SERVERBOUND(0x10,PacketDirection.SERVERBOUND),

    RESPAWN(0x39 ,PacketDirection.CLIENTBOUND),
    JOIN_GAME(0x24 , PacketDirection.CLIENTBOUND );

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
