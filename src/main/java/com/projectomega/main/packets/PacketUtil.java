package com.projectomega.main.packets;

import com.projectomega.main.ServerThread;
import com.projectomega.main.debugging.DebuggingUtil;
import com.projectomega.main.packets.datatype.UnsignedByte;
import com.projectomega.main.packets.datatype.VarInt;
import com.projectomega.main.packets.datatype.VarLong;
import com.projectomega.main.packets.types.PacketHandshake;
import com.projectomega.main.packets.types.PacketInboundChat;
import com.projectomega.main.packets.types.PacketKeepAlive;
import com.projectomega.main.packets.types.PacketPing;
import com.projectomega.main.utils.ByteUtils;
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
        handlers.put(PacketType.KEEP_ALIVE_SERVERBOUND_OLD, new ArrayList<>(Arrays.asList(new PacketKeepAlive())));
        handlers.put(PacketType.CHAT_SERVERBOUND, new ArrayList<>(Arrays.asList(new PacketInboundChat())));
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
        offset += ByteUtils.addVarIntToByteArray(bytes, offset, packet.getType().getId());
        for (int dataIndex = 0; dataIndex < packet.getDataLength(); dataIndex++) {
            Object data = packet.getData(dataIndex);
            if (DebuggingUtil.DEBUG)
                System.out.println("Writing " + data + " to index " + offset);
            if (data instanceof VarInt) {
                offset += ByteUtils.addVarIntToByteArray(bytes, offset, ((VarInt) data).getInteger());
            } else if (data instanceof Integer) {
                offset += ByteUtils.addIntToByteArray(bytes, offset, (Integer) data);
            } else if (data instanceof VarLong) {
                offset += writeVarLong(bytes, offset, ((VarLong) data).getLong());
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
            } else if (data instanceof NBTCompound) {
                try {
                    //byte[] byteArray = NBTWriter.writeToBase64((NBTCompound) data,false);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    NBTWriter.write((NBTCompound) data, out, false);
                    byte[] byteArray = out.toByteArray();
                    //offset += ByteUtils.addByteToByteArray(bytes,offset, (byte) 10);//byteArray.length);
                    //offset += ByteUtils.addByteToByteArray(bytes,offset, (byte) 0x10);
                    //offset += ByteUtils.addByteToByteArray(bytes,offset, (byte) 0);
                    //offset += ByteUtils.addByteToByteArray(bytes,offset, (byte) 8);
                    if (DebuggingUtil.DEBUG) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(byteArray.length + "||");
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Char||");
                        for (int i = 0; i < byteArray.length; i++) {
                            sb.append(" " + byteArray[i]);
                        }
                        for (int i = 0; i < byteArray.length; i++) {
                            char c = (char) byteArray[i];
                            if (DebuggingUtil.isCharacter(c)) {
                                sb2.append((char) byteArray[i]);
                            } else {
                                if (i + 2 < byteArray.length) {
                                    if (DebuggingUtil.isCharacter((char) byteArray[i + 2])) {
                                        if (DebuggingUtil.isCharacter((char) byteArray[i + 1])) {
                                            sb2.append(":" + byteArray[i] + ")");
                                        } else {
                                            sb2.append("(" + byteArray[i]);
                                        }
                                    } else {
                                        sb2.append(" " + byteArray[i]);
                                    }
                                } else {
                                    sb2.append(" " + byteArray[i]);
                                }
                            }
                        }
                        System.out.println(sb.toString());
                        System.out.println(sb2.toString());
                    }
                    /*offset+= writeByte(bytes,offset, (byte) 10);
                    offset+= writeByte(bytes,offset, (byte) 0);
                    offset+= writeByte(bytes,offset, (byte) 0);
                    offset+= writeByte(bytes,offset, (byte) 0);*/
                    //offset += writeShort(bytes,offset, (short) byteArray.length);
                    offset += writeBytes(bytes, offset, byteArray);
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
        int l = ByteUtils.addVarIntToByteArray(varIntLength, 0, length);
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
        try {
            if(DebuggingUtil.DEBUG)
            System.out.println("Writing " + length + " bytes for  " + packet.getType().getId() + "  || " + bytebuf.array().length);
            connection.writeAndFlush(bytebuf).awaitUninterruptibly().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int writeShort(byte[] bytes, int offset, short length) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(length);
        for(int i = 0; i < 2 ; i++) {
            bytes[offset+i] = buffer.get(i);
        }
        return 2;
    }

    private static int writeByte(byte[] bytes, int offset, byte i) {
        bytes[offset]=i;
        return 1;
    }

    private static int writeBytes(byte[] bytes, int offset, byte[] writeToBase64) {
        for (int i = 0; i < writeToBase64.length; i++) {
            bytes[offset + i] = writeToBase64[i];
        }
        return writeToBase64.length;
    }

    private static int writeString(byte[] bytes, int offset, String data) {
        int offset2 = 0;
        offset2 += ByteUtils.addVarIntToByteArray(bytes, offset, data.getBytes().length);
        System.out.println("offset2 " + offset2 + " bytes|Fulloffeset " + (offset + offset2) + " length " + data.getBytes().length);
        offset2 += ByteUtils.addStringToByteArray(bytes, offset + offset2, data);
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

    public static boolean readBoolean(ByteBuf bytebuf) {
        return bytebuf.readByte()==0x01;
    }

    public static byte readUnsignedByte(ByteBuf bytebuf) {
        return bytebuf.readByte();
    }
}
