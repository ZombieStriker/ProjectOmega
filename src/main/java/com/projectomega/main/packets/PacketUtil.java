package com.projectomega.main.packets;

import com.projectomega.main.ServerThread;
import com.projectomega.main.packets.datatype.UnsignedByte;
import com.projectomega.main.packets.types.PacketHandshake;
import com.projectomega.main.packets.types.PacketPing;
import com.projectomega.main.utils.ByteUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import me.nullicorn.nedit.NBTWriter;
import me.nullicorn.nedit.type.NBTCompound;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class PacketUtil {

    private static HashMap<PacketType, List<PacketHandler>> handlers = new HashMap<>();
    private static ServerThread server;

    public static void init(ServerThread serverThread) {
        handlers.put(PacketType.HANDSHAKE, new ArrayList<>(Arrays.asList(new PacketHandshake())));
        handlers.put(PacketType.HANDSHAKE_PING, new ArrayList<>(Arrays.asList(new PacketPing())));
        server = serverThread;
    }

    public static int readVarInt(ByteBuf byteBuf) {
        int numRead = 0;
        int result = 0;
        byte read;
        do {
            read = byteBuf.readByte();
            int value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            if (numRead > 5) {
                throw new RuntimeException("VarInt is too big");
            }
        } while ((read & 0b10000000) != 0);

        return result;
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

    public static int writeVarInt(byte[] bytes, int offset, int value) {
        int i = 0;
        do {
            byte temp = (byte) (value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            bytes[offset + i] = temp;
            i++;
        } while (value != 0);
        return i;
    }

    public static int writeVarLong(byte[] bytes, int offset, long value) {
        int i = 0;
        do {
            byte temp = (byte) (value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            bytes[offset + i] = temp;
            i++;
        } while (value != 0);
        return i;
    }

    public static int writeLong(byte[] bytes, int offset, long value) {
        int i = 0;
        do {
            byte temp = (byte) (value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            bytes[offset + i] = temp;
            i++;
        } while (i != 8);
        return i;
    }

    public static void writePacketToOutputStream(Channel connection, OutboundPacket packet) {
        byte[] bytes = new byte[256 * 256 * 256];
        int length = 0;
        int offset = 0;
        offset += ByteUtils.addIntegerToByteArray(bytes, offset, packet.getType().getId());
        for (int dataIndex = 0; dataIndex < packet.getDataLength(); dataIndex++) {
            Object data = packet.getData(dataIndex);
            if (data instanceof Integer) {
                offset += ByteUtils.addIntegerToByteArray(bytes, offset, (Integer) data);
            } else if (data instanceof Long) {
                offset += ByteUtils.addLongToByteArray(bytes, offset, (Long) data);
            } else if (data instanceof Short) {
                offset += ByteUtils.addShortToByteArray(bytes, offset, (short) data);
            } else if (data instanceof UUID) {
                offset += writeUUID(bytes, offset, (UUID) data);
            }else if (data instanceof NBTCompound){
                try {
                    offset += writeBytes(bytes,offset, NBTWriter.writeToBase64((NBTCompound) data));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (data instanceof Byte) {
                offset += ByteUtils.addByteToByteArray(bytes, offset, (Byte) data);
            } else if (data instanceof UnsignedByte) {
                offset += ByteUtils.addByteToByteArray(bytes, offset, ((UnsignedByte) data).getUnsignedByte());
            } else if (data instanceof Boolean) {
                offset += ByteUtils.addByteToByteArray(bytes, offset, (byte) (((Boolean) data) ? 0x01 : 0x00));
            } else if (data instanceof String) {
                offset += writeString(bytes, offset, (String) data);
            }
        }
        length = offset;
        byte[] varIntLength = new byte[3];
        int l = ByteUtils.addIntegerToByteArray(varIntLength, 0, length);
        ByteBuf bytebuf = Unpooled.buffer();
        for (int i = 0; i < l; i++) {
            bytebuf.writeByte(varIntLength[i]);
        }
        for (int i = 0; i < length; i++) {
            bytebuf.writeByte(bytes[i]);
        }
        bytebuf.writeByte(-1);
        try {
            connection.writeAndFlush(bytebuf).awaitUninterruptibly().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int writeBytes(byte[] bytes, int offset, byte[] writeToBase64) {
        for(int i = 0; i < writeToBase64.length; i++){
            bytes[offset+i]=writeToBase64[i];
        }
        return writeToBase64.length;
    }

    private static int writeString(byte[] bytes, int offset, String data) {
        int offset2 = 0;
        offset2 += ByteUtils.addIntegerToByteArray(bytes, offset + offset2, (int) ((String) data).getBytes().length);
        offset2 += ByteUtils.addStringToByteArray(bytes, offset + offset2, (String) data);
        return offset2;
    }

    public static List<PacketHandler> getPacketHandlersByID(int packetid) {
        for (Map.Entry<PacketType, List<PacketHandler>> packetHandler : handlers.entrySet()) {
            if (packetHandler.getKey().getId() == packetid) {
                return packetHandler.getValue();
            }
        }
        return null;
    }

    public static long readLong(ByteBuf byteBuf) {
        int numRead = 0;
        long result = 0;
        byte read;
        do {
            read = byteBuf.readByte();
            long value = (read & 0b01111111);
            result |= (value << (7 * numRead));

            numRead++;
            System.out.println(numRead + "||" + result);
            if (numRead > 10) {
                throw new RuntimeException("VarLong is too big");
            }
        } while (numRead != 8);

        return result;
    }

    public static int writeUUID(byte[] bytes, int offset, UUID uuid) {
        int i = 0;
        long value = uuid.getMostSignificantBits();
        long value2 = uuid.getLeastSignificantBits();
        i += writeUUID(bytes, offset, value, value2);
        return i;
    }

    private static int writeUUID(byte[] bytes, int offset, long value, long value2) {
        int i = 0;
        do {
            byte temp;
            if (i < 8) {
                temp = (byte) (value & 0b01111111);
            } else {
                temp = (byte) (value2 & 0b01111111);
            }
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            bytes[offset + i] = temp;
            i++;
        } while (i != 16);
        return i;
    }
}
