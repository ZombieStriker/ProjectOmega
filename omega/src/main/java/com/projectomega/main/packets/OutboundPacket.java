package com.projectomega.main.packets;

public class OutboundPacket {

    private Object[] array;
    private PacketType type;
    private int protcolVersion;

    public OutboundPacket(PacketType type, int protcolVersion, Object... data) {
        this.type = type;
        this.array = data;
        this.protcolVersion = protcolVersion;
    }

    public int getProtcolVersion() {
        return protcolVersion;
    }
    public void setData(Object... array){
        this.array = array;
    }

    public PacketType getType() {
        return type;
    }

    public Object getData(int index) {
        return array[index];
    }

    public int getDataLength() {
        return array.length;
    }
}
