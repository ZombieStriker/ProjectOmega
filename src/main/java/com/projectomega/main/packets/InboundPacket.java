package com.projectomega.main.packets;

import io.netty.channel.Channel;

import java.net.SocketAddress;

public class InboundPacket {

    private Object[] array;
    private PacketType type;
    private Channel receiver = null;

    private boolean cancel = false;

    public InboundPacket(PacketType type, Object[] data, Channel receiver){
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

    public Channel getAddress(){
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
