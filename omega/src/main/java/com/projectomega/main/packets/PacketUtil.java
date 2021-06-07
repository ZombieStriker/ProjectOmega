package com.projectomega.main.packets;

import com.projectomega.main.ServerThread;
import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.game.Block;
import com.projectomega.main.game.Chunk;
import com.projectomega.main.game.Omega;
import com.projectomega.main.game.Player;
import com.projectomega.main.game.entity.EntityType;
import com.projectomega.main.packets.datatype.*;
import com.projectomega.main.packets.types.*;
import com.projectomega.main.versions.ProtocolManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import me.nullicorn.nedit.NBTWriter;
import me.nullicorn.nedit.type.NBTCompound;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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

    public static int writeInt(ByteBuf bytes, int offset, int value) {
        return writeInt(bytes, offset, value, false);
    }

    public static int writeInt(ByteBuf bytes, int offset, int value, boolean invert) {
        bytes.writeInt(value);
        return 4;
    }

    public static int writeVarInt(ByteBuf bytes, int offset, int value) {
        int i = 0;
        do {
            byte temp = (byte) (value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            bytes.writeByte(temp);
            i++;
        } while (value != 0);
        return i;
    }

    public static int writeVarLong(ByteBuf bytes, int offset, long value) {
        int i = 0;
        do {
            byte temp = (byte) (value & 0b01111111);
            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
            if (value != 0) {
                temp |= 0b10000000;
            }
            bytes.writeByte(temp);
            i++;
        } while (value != 0);
        return i;
    }

    public static int writeLong(ByteBuf bytes, int offset, long value) {
        bytes.writeLong(value);
        return 8;
    }

    public static void writePacketToOutputStream(Channel connection, OutboundPacket packet) {
        ByteBuf bytes = Unpooled.buffer();
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
                offset += addIntToByteArray(bytes, offset, (Integer) data);
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
                offset += addShortToByteArray(bytes, offset, (Short) data);
            } else if (data instanceof Double) {
                offset += addDoubleToByteArray(bytes, offset, (Double) data);
            } else if (data instanceof Float) {
                offset += addFloatToByteArray(bytes, offset, (Float) data);
            } else if (data instanceof UUID) {
                offset += writeUUID(bytes, offset, (UUID) data);
            } else if (data instanceof MetaData) {
                offset += writeBytes(bytes, offset, ((MetaData) data).build());
            } else if (data instanceof Slot[]) {
                for(Slot slot : (Slot[])data) {
                    offset += addByteToByteArray(bytes, offset, (byte) (slot.isItem() ? 0x01 : 0x00));
                    if (slot.isItem()) {
                        offset += writeVarInt(bytes, offset, slot.getId());
                        offset += writeByte(bytes, offset, slot.getAmount());
                        try {
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            NBTWriter.write(slot.getNBT(), out, false);
                            byte[] byteArray = out.toByteArray();
                            offset += writeBytes(bytes, offset, byteArray);
                            offset += writeByte(bytes, offset, (byte) 0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (data instanceof Slot) {
                offset += addByteToByteArray(bytes, offset, (byte) (((Slot) data).isItem() ? 0x01 : 0x00));
                if (((Slot) data).isItem()) {
                    offset += writeVarInt(bytes, offset, ((Slot) data).getId());
                    offset += writeByte(bytes, offset, ((Slot) data).getAmount());
                    try {
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        NBTWriter.write(((Slot) data).getNBT(), out, false);
                        byte[] byteArray = out.toByteArray();
                        offset += writeBytes(bytes, offset, byteArray);
                        offset += writeByte(bytes, offset, (byte) 0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
                offset += addByteToByteArray(bytes, offset, (Byte) data);
            } else if (data instanceof UnsignedByte) {
                offset += addByteToByteArray(bytes, offset, ((UnsignedByte) data).getUnsignedByte());
            } else if (data instanceof Boolean) {
                offset += addByteToByteArray(bytes, offset, (byte) (((Boolean) data) ? 0x01 : 0x00));
            } else if (data instanceof String) {
                offset += writeString(bytes, offset, (String) data);
            }
        }
        length = offset;
        ByteBuf bytebuf = Unpooled.buffer();
        writeVarInt(bytebuf,0,length);
        for (int i = 0; i < length; i++) {
            bytebuf.writeByte(bytes.getByte(i));
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

    private static int writeShort(ByteBuf bytes, int offset, short length) {
        bytes.writeShort(length);
        return 2;
    }

    private static int writeByte(ByteBuf bytes, int offset, byte i) {
        bytes.writeByte(i);
        return 1;
    }

    public static int writeBytes(ByteBuf bytes, int offset, byte[] writeToBase64) {
        bytes.writeBytes(writeToBase64);
        return writeToBase64.length;
    }

    private static int writeString(ByteBuf bytes, int offset, String data) {
        int offset2 = 0;
        offset2 += writeVarInt(bytes, offset, data.getBytes().length);
        offset2 += addStringToByteArray(bytes, offset + offset2, data);
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

    public static int writeUUID(ByteBuf bytes, int offset, UUID uuid) {
        int i = 0;
        long value = uuid.getMostSignificantBits();
        long value2 = uuid.getLeastSignificantBits();
        i += writeUUID(bytes, offset, value, value2);
        return i;
    }

    private static int writeUUID(ByteBuf bytes, int offset, long value, long value2) {
       /* int i = 0;
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
        return i;*/
        bytes.writeLong(value);
        bytes.writeLong(value2);
        return 16;
    }

    public static boolean readBoolean(ByteBuf bytebuf) {
        return bytebuf.readByte() == 0x01;
    }

    public static byte readUnsignedByte(ByteBuf bytebuf) {
        return bytebuf.readByte();
    }


    public static int addShortToByteArray(ByteBuf bytes, int offset, short number) {
        bytes.writeShort(number);
        return 2;
    }

    public static int addStringToByteArray(ByteBuf bytes, int offset, String message) {
        byte[] chars = message.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < chars.length; i++) {
            bytes.writeByte(chars[i]);
        }
        return chars.length;
    }

    public static String buildString(ByteBuf buf, int size) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            try {
                sb.append((char) buf.readByte());
            } catch (IndexOutOfBoundsException e4) {
                e4.printStackTrace();
                break;
            }
        }
        return sb.toString();
    }

    public static int addByteToByteArray(ByteBuf bytes, int offset, byte length) {
        bytes.writeByte(length);
        return 1;
    }

    public static int addIntToByteArray(ByteBuf bytes, int offset, Integer data) {
        bytes.writeInt(data);
        return 4;
    }

    public static int addFloatToByteArray(ByteBuf bytes, int offset, float data) {
        bytes.writeFloat(data);
        return 4;
    }

    public static long getChunkSectionPositionAsALong(Chunk chunk, int y) {
        System.out.println(((((long) chunk.getX()) & 0x3FFFFF) << 42) | (y & 0xFFFFF) | ((((long) chunk.getZ()) & 0x3FFFFF) << 20));
        return ((((long) chunk.getX()) & 0x3FFFFF) << 42) | (y & 0xFFFFF) | ((((long) chunk.getZ()) & 0x3FFFFF) << 20);
    }

    public static VarLong encodeBlockToBlocksArray(int protocolversion, Block block) {
        int x = block.getLocation().getBlockX();
        int y = block.getLocation().getBlockY();
        int z = block.getLocation().getBlockZ();
        /*if(x < 0) {
            x = -x;
        }
        if(y < 0) {
            y = -y;
        }
        if(z < 0) {
            z = -z;
        }*/
        byte blockLocalX = (byte) (x & 0x0F);
        byte blockLocalY = (byte) (y & 0x0F);
        byte blockLocalZ = (byte) (z & 0x0F);
        long blockid = (ProtocolManager.getBlockIDByType(protocolversion, block.getType()));
        if (blockid == -1)
            blockid = 1;
        return new VarLong(blockid << 12 | (blockLocalX << 8 | blockLocalZ << 4 | blockLocalY));
    }

    public static int addDoubleToByteArray(ByteBuf bytes, int offset, double data) {
        bytes.writeDouble(data);
        return 8;
    }

    public static String buildString(ByteBuf bytebuf) {
        return buildString(bytebuf, PacketUtil.readVarInt(bytebuf));
    }
}
