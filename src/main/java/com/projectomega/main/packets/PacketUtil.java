package com.projectomega.main.packets;

import com.projectomega.main.ServerThread;
import com.projectomega.main.packets.types.Packet_Handshake;
import com.projectomega.main.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.net.SocketAddress;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class PacketUtil {

    private static HashMap<PacketType, List<PacketHandler>> handlers = new HashMap<>();
    private static ServerThread server;

    public static void init(ServerThread serverThread) {
        handlers.put(PacketType.HANDSHAKE, new ArrayList<>(Arrays.asList(new Packet_Handshake())));
        server = serverThread;
    }

    public static void writePacketToOutputStream(SocketAddress connection, OutboundPacket packet) {
        byte[] bytes = new byte[256 * 9];
        int length = 0;
        int offset = 2;
        length = (offset += ByteUtils.addShortToByteArray(bytes, offset, (short) packet.getType().getId()));
        for (int dataIndex = 0; dataIndex < packet.getDataLength(); dataIndex++) {
            Object data = packet.getData(dataIndex);
            if (data instanceof Integer) {
                length = (offset += ByteUtils.addIntegerToByteArray(bytes, offset, (Integer) data));
            } else if (data instanceof Short) {
                length = (offset += ByteUtils.addShortToByteArray(bytes, offset, (short) data));
            } else if (data instanceof String) {
                length = (offset += ByteUtils.addByteToByteArray(bytes, offset, (byte) ((String) data).getBytes().length));
                length = (offset += ByteUtils.addStringToByteArray(bytes, offset, (String) data));
            }
        }
        ByteUtils.addByteToByteArray(bytes, 0, (byte) length);
        ByteBuf bytebuf = Unpooled.buffer();
        for (int i = 0; i < bytes.length; i++) {
            bytebuf.writeByte(bytes[i]);
        }


        ChannelFuture channel = server.getConnection(connection);
        if (channel == null) {
            channel = server.createConnection(connection);
        }
        try {
            channel.sync().channel().writeAndFlush(bytebuf);
            channel.await(1L, TimeUnit.MILLISECONDS);
            channel.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static List<PacketHandler> getPacketHandlersByID(int packetid) {
        for (Map.Entry<PacketType, List<PacketHandler>> packetHandler : handlers.entrySet()) {
            if (packetHandler.getKey().getId() == packetid) {
                return packetHandler.getValue();
            }
        }
        return null;
    }
}
