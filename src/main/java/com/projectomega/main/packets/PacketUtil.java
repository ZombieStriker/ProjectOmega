package com.projectomega.main.packets;

import com.projectomega.main.ServerThread;
import com.projectomega.main.packets.types.PacketHandshake;
import com.projectomega.main.packets.types.PacketPing;
import com.projectomega.main.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

import java.util.*;

public class PacketUtil {

    private static HashMap<PacketType, List<PacketHandler>> handlers = new HashMap<>();
    private static ServerThread server;

    public static void init(ServerThread serverThread) {
        handlers.put(PacketType.HANDSHAKE, new ArrayList<>(Arrays.asList(new PacketHandshake())));
        handlers.put(PacketType.HANDSHAKE_PING, new ArrayList<>(Arrays.asList(new PacketPing())));
        server = serverThread;
    }

    public static long readVarLong(ByteBuf byteBuf) {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = byteBuf.readByte();
            long value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 10) {
                throw new RuntimeException("VarLong is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
    }

    public static void writePacketToOutputStream(Channel connection, OutboundPacket packet) {
        byte[] bytes = new byte[256 * 2];
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
        for (int i = 0; i < length; i++) {
            bytebuf.writeByte(bytes[i]);
        }
        try {
           connection.writeAndFlush(bytebuf).awaitUninterruptibly().sync();
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
