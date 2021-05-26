package com.projectomega.main;

import com.projectomega.main.packets.PacketHandler;
import com.projectomega.main.packets.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

public class ServerInputHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bytebuf = (ByteBuf) msg;
        int size = bytebuf.readByte();
        int packetid = bytebuf.readByte();
        List<PacketHandler> packethandlers = PacketUtil.getPacketHandlersByID(packetid);
        if (packethandlers != null) {
            System.out.println("Packet Handlers found for "+packetid+" : "+packethandlers.size());
            for (PacketHandler packetHandler : packethandlers) {
                packetHandler.call(bytebuf, size - 1, ctx.channel().remoteAddress(),ctx.channel());
            }
        }else{
            System.out.println("Failed to find packetHandler for packet "+packetid);
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
