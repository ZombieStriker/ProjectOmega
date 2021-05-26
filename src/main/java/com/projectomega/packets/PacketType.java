package com.projectomega.packets;

public enum PacketType {

    HANDSHAKE(0)
    ;

    private int id;

    PacketType(int type){
        this.id= type;
    }

    public int getId(){
        return id;
    }
}
