package com.projectomega.main.packets;

import java.net.SocketAddress;

public class InboundPacket {

    private Object[] array;
    private PacketType type;
    private SocketAddress receiver = null;

    private boolean cancel = false;

    public InboundPacket(PacketType type, Object[] data, SocketAddress receiver){
        this.type = type;
        this.array = data;
        this.receiver = receiver;
    }

    public void setCancelled(boolean cancel){
        this.cancel = cancel;
    }
    public boolean isCancelled(){
        return cancel;
    }

    public SocketAddress getAddress(){
        return receiver;
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
