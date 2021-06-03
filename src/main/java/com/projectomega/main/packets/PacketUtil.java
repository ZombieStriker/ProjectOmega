package com.projectomega.main.packets;

import com.projectomega.main.ServerThread;
import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.packets.datatype.*;
import com.projectomega.main.packets.types.*;
import com.projectomega.main.utils.ByteUtils;
import com.projectomega.main.versions.ProtocolManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import me.nullicorn.nedit.NBTWriter;
import me.nullicorn.nedit.type.NBTCompound;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class PacketUtil {

    private static HashMap<PacketType, List<PacketHandler>> handlers = new HashMap<>();
    private static ServerThread server;

    public static void init(ServerThread serverThread) {
        handlers.put(PacketType.HANDSHAKE, new ArrayList<>(Arrays.asList(new PacketHandshake())));
        handlers.put(PacketType.HANDSHAKE_PING, new ArrayList<>(Arrays.asList(new PacketPing())));
        handlers.put(PacketType.KEEP_ALIVE_SERVERBOUND, new ArrayList<>(Arrays.asList(new PacketKeepAlive())));
        handlers.put(PacketType.CHAT_SERVERBOUND, new ArrayList<>(Arrays.asList(new PacketInboundChat())));
        handlers.put(PacketType.CLIENT_SETTINGS, new ArrayList<>(Arrays.asList(new PacketClientSettings())));
        handlers.put(PacketType.CLICK_WINDOW, new ArrayList<>(Arrays.asList(new PacketClickWindow())));
        handlers.put(PacketType.CLIENT_STATUS, new ArrayList<>(Arrays.asList(new PacketClientStatus())));
        handlers.put(PacketType.HELD_ITEM_CHANGE_SERVERBOUND, new ArrayList<>(Arrays.asList(new PacketHeldItemChange())));
        handlers.put(PacketType.PLAYER_POSITION, new ArrayList<>(Arrays.asList(new PacketPlayerPosition())));
        handlers.put(PacketType.PLAYER_POSITION_AND_ROTATION, new ArrayList<>(Arrays.asList(new PacketPlayerPositionAndRotation())));
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

    public static int writeInt(byte[] bytes, int offset, int value) {
        return writeInt(bytes, offset, value, false);
    }

    public static int writeInt(byte[] bytes, int offset, int value, boolean invert) {
        int i = 0;
        do {
            byte temp = (byte) (value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            if (invert)
                bytes[offset + 3 - i] = temp;
            else
                bytes[offset + i] = temp;
            i++;
        } while (i != 4);
        return i;
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
            byte temp = (byte) (value & 0b11111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            /*if (value != 0) {
                temp |= 0b10000000;
            }*/
            bytes[offset + i] = temp;
            i++;
        } while (i != 8);
        return i;
    }

    public static void writePacketToOutputStream(Channel connection, OutboundPacket packet) {
        byte[] bytes = new byte[256 * 256 * 256];
        int length = 0;
        int offset = 0;
        Player player = Omega.getPlayerByChannel(connection);
        int protocolVersion = 0;
        if (player != null) {
            protocolVersion = player.getProtocolVersion();
        }
        int packetid = ProtocolManager.getPacketIDForProtocol(protocolVersion, packet.getType());
        offset += writeVarInt(bytes, offset, packetid);
        for (int dataIndex = 0; dataIndex < packet.getDataLength(); dataIndex++) {
            Object data = packet.getData(dataIndex);
            if (DebuggingUtil.DEBUG)
                System.out.println("Writing " + data + " to index " + offset);
            if (data instanceof VarInt) {
                offset += writeVarInt(bytes, offset, ((VarInt) data).getInteger());
            } else if (data instanceof EntityType) {
                offset += writeVarInt(bytes, offset, new VarInt(ProtocolManager.getEntityIDForProtocol(protocolVersion, (EntityType) data)).getInteger());
            } else if (data instanceof Integer) {
                offset += ByteUtils.addIntToByteArray(bytes, offset, (Integer) data);
            } else if (data instanceof VarLong) {
                offset += writeVarLong(bytes, offset, ((VarLong) data).getLong());
            } else if (data instanceof Angle) {
                offset += writeByte(bytes, offset, ((Angle) data).getValue());
            } else if (data instanceof byte[]) {
                offset += writeBytes(bytes, offset, (byte[]) data);
            } else if (data instanceof Position) {
                offset += writeBytes(bytes, offset, ((Position) data).build());
            } else if (data instanceof VarInt[]) {
                for (int i = 0; i < ((VarInt[]) data).length; i++)
                    offset += writeVarInt(bytes, offset, ((VarInt[]) data)[i].getInteger());
            } else if (data instanceof VarLong[]) {
                for (int i = 0; i < ((VarLong[]) data).length; i++)
                    offset += writeVarLong(bytes, offset, ((VarLong[]) data)[i].getLong());
            } else if (data instanceof int[]) {
                for (int i = 0; i < ((int[]) data).length; i++)
                    offset += writeInt(bytes, offset, ((int[]) data)[i]);
            } else if (data instanceof Long) {
                offset += writeLong(bytes, offset, (Long) data);
            } else if (data instanceof Short) {
                offset += ByteUtils.addShortToByteArray(bytes, offset, (Short) data);
            } else if (data instanceof Double) {
                offset += ByteUtils.addDoubleToByteArray(bytes, offset, (Double) data);
            } else if (data instanceof Float) {
                offset += ByteUtils.addFloatToByteArray(bytes, offset, (Float) data);
            } else if (data instanceof UUID) {
                offset += writeUUID(bytes, offset, (UUID) data);
            } else if (data instanceof MetaData) {
                offset += writeBytes(bytes, offset, ((MetaData) data).build());
            } else if (data instanceof NBTCompound) {
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    NBTWriter.write((NBTCompound) data, out, false);
                    byte[] byteArray = out.toByteArray();
                    offset += writeBytes(bytes, offset, byteArray);
                    offset += writeByte(bytes, offset, (byte) 0);
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
        if (length >= 2097151) {
            System.out.println("Packet to long. Dumping data:");
            System.out.println(DebuggingUtil.dumpBytes(bytes, length));
        }
        int l = writeVarInt(varIntLength, 0, length);
        ByteBuf bytebuf = Unpooled.buffer();
        for (int i = 0; i < l; i++) {
            bytebuf.writeByte(varIntLength[i]);
        }
        for (int i = 0; i < length; i++) {
            bytebuf.writeByte(bytes[i]);
        }
        //  if(packet.getType()==PacketType.HANDSHAKE)
        bytebuf.writeByte(0);
        bytebuf.writeByte(0);
        bytebuf.writeByte(0);

        List<PacketHandler> packethandlers = PacketUtil.getPacketHandlersBy(packet.getType());
        if (packethandlers != null) {
            for (PacketHandler packetHandler : packethandlers) {
                packetHandler.call(bytebuf, length, connection);
            }
        }


        try {
            if (DebuggingUtil.DEBUG)
                System.out.println("Writing " + length + " bytes for packetid: " + packetid + "  || " + bytebuf.array().length);
            connection.writeAndFlush(bytebuf).awaitUninterruptibly().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int writeShort(byte[] bytes, int offset, short length) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(length);
        for (int i = 0; i < 2; i++) {
            bytes[offset + i] = buffer.get(i);
        }
        return 2;
    }

    private static int writeByte(byte[] bytes, int offset, byte i) {
        bytes[offset] = i;
        return 1;
    }

    public static int writeBytes(byte[] bytes, int offset, byte[] writeToBase64) {
        for (int i = 0; i < writeToBase64.length; i++) {
            bytes[offset + i] = writeToBase64[i];
        }
        return writeToBase64.length;
    }

    private static int writeString(byte[] bytes, int offset, String data) {
        int offset2 = 0;
        offset2 += writeVarInt(bytes, offset, data.getBytes().length);
        offset2 += ByteUtils.addStringToByteArray(bytes, offset + offset2, data);
        return offset2;
    }

    public static List<PacketHandler> getPacketHandlersBy(PacketType type) {
        for (Map.Entry<PacketType, List<PacketHandler>> packetHandler : handlers.entrySet()) {
            if (packetHandler.getKey() == type) {
                return packetHandler.getValue();
            }
        }
        return null;
    }

    public static long readLong(ByteBuf byteBuf) {
        return byteBuf.readLong();
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

    public static boolean readBoolean(ByteBuf bytebuf) {
        return bytebuf.readByte() == 0x01;
    }

    public static byte readUnsignedByte(ByteBuf bytebuf) {
        return bytebuf.readByte();
    }
}
