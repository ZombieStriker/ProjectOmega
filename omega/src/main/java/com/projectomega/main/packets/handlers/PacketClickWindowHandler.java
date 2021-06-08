package com.projectomega.main.packets.handlers;

import com.projectomega.main.packets.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.Channel;
import me.nullicorn.nedit.NBTReader;
import me.nullicorn.nedit.type.NBTCompound;

import java.io.IOException;
import java.util.List;

public class PacketClickWindowHandler extends PacketHandler {
    public PacketClickWindowHandler() {
        super(PacketType.CLICK_WINDOW);
    }

    @Override
    public void call(ByteBuf bytebuf, int i, Channel ctx) {
        byte windowid = bytebuf.readByte();
        short slot = bytebuf.readShort();
        byte button = bytebuf.readByte();
        short actionnumber = bytebuf.readShort();
        int mode = PacketUtil.readVarInt(bytebuf);

        boolean present = PacketUtil.readBoolean(bytebuf);
        int itemid = 0;
        byte itemcount = 0;
        NBTCompound compound = null;
        if(present){
            itemid = PacketUtil.readVarInt(bytebuf);
            itemcount = bytebuf.readByte();
            try {
                compound = NBTReader.read(new ByteBufInputStream(bytebuf));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InboundPacket packet = new InboundPacket(PacketType.CLICK_WINDOW,new Object[]{windowid,slot,button,actionnumber,mode,present,itemid,itemcount,compound},ctx);
        List<PacketListener> packetlisteners = PacketManager.getListeners(PacketType.CLICK_WINDOW);
        if(packetlisteners!=null){
            System.out.println(packetlisteners.size());
            for(PacketListener listener : packetlisteners){
                listener.onCall(packet);
            }
        }

    }
}
