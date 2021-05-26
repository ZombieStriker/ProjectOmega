package com.projectomega.main.packets;

public enum PacketType {

    HANDSHAKE(0, PacketDirection.SERVERBOUND),
    STATUS_PING(0, PacketDirection.CLIENTBOUND),
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
