package com.projectomega.main;

import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.packets.PacketHandler;
import com.projectomega.main.packets.PacketType;
import com.projectomega.main.packets.PacketUtil;
import com.projectomega.main.versions.ProtocolManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.List;

public class ServerInputHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf bytebuf = (ByteBuf) msg;
        int size = bytebuf.readByte();
        if(size > 1) {
            int packetid = bytebuf.readByte();
            Player player = Omega.getPlayerByChannel(ctx.channel());
            int protocolversion = 0;
            if (player != null) {
                protocolversion = player.getProtocolVersion();
            }
            List<PacketHandler> packethandlers = PacketUtil.getPacketHandlersBy(ProtocolManager.getPacketByID(protocolversion, packetid, player == null, PacketType.PacketDirection.SERVERBOUND));
            if (packethandlers != null) {
                if(DebuggingUtil.DEBUG)
                    System.out.println("Packet Handlers found for " + packetid + " : " + packethandlers.size());
                for (PacketHandler packetHandler : packethandlers) {
                    packetHandler.call(bytebuf, size - 1, ctx.channel());
                }
            } else {
                System.out.println("Failed to find packetHandler for packet " + packetid + ":" + protocolversion);
            }
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
