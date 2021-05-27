package com.projectomega.main.packets;

import io.netty.channel.Channel;

public class InboundPacket {

    private Object[] array;
    private PacketType type;
    private Channel channel = null;

    private boolean cancel = false;

    public InboundPacket(PacketType type, Object[] data, Channel channel){
        this.type = type;
        this.array = data;
        this.channel = channel;
    }

    public void setCancelled(boolean cancel){
        this.cancel = cancel;
    }
    public boolean isCancelled(){
        return cancel;
    }

    public Channel getChannel(){
        return channel;
    }

    public PacketType getType(){
        return type;
    }
    public Object getData(int index){
        if(index >= getDataLength())
            return null;
        return array[index];
    }
    public int getDataLength(){
        return array.length;
    }
}
