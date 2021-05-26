package com.projectomega.main.packets;

public class OutboundPacket {

    private Object[] array;
    private PacketType type;

    public OutboundPacket(PacketType type, Object[] data){
        this.type = type;
        this.array = data;
    }

    public PacketType getType(){
        return type;
    }
    public Object getData(int index){
        return array[index];
    }
    public int getDataLength(){
        return array.length;
    }
}
