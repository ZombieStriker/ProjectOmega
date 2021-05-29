package com.projectomega.main.utils;

import com.projectomega.main.packets.PacketUtil;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteUtils {

    public static int addVarIntToByteArray(byte[] bytes, int offset, int number){
        return  PacketUtil.writeVarInt(bytes,offset,number);
    }
    public static int addVarLongToByteArray(byte[] bytes, int offset, long number){
        return  PacketUtil.writeVarLong(bytes,offset,number);
    }
    public static int addShortToByteArray(byte[] bytes,int offset, short number){
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(number);
        for(int i = 0; i < 2 ; i++) {
            bytes[offset+i] = buffer.get(i);
        }
        return 2;
    }
    public static int addStringToByteArray(byte[] bytes,int offset, String message){
        byte[] chars = message.getBytes(StandardCharsets.UTF_8);
        for(int i = 0; i < chars.length ; i++) {
            bytes[offset+i] = chars[i];
        }
        return chars.length;
    }
    public static String buildString(ByteBuf buf, int size){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i++){
            sb.append((char)buf.readByte());
        }
        return sb.toString();
    }
    public static int buildInt(ByteBuf buf){
      return buf.readByte()|buf.readByte()|buf.readByte();
    }

    public static int addByteToByteArray(byte[] bytes, int offset, byte length) {
        bytes[offset]=length;
        return 1;
    }

    public static int addIntToByteArray(byte[] bytes, int offset, Integer data) {
        return PacketUtil.writeInt(bytes,offset,data);
    }

    public static int addFloatToByteArray(byte[] bytes, int offset, float data) {
        int floatingPointNumber = (int) (data * 32);
        return addIntToByteArray(bytes,offset,floatingPointNumber);
    }

    public static int addDoubleToByteArray(byte[] bytes, int offset, double data) {
        long doubleFloatingPointNumber = (long) (data*(0x34));
        return PacketUtil.writeLong(bytes,offset,doubleFloatingPointNumber);
    }

    public static String buildString(ByteBuf bytebuf) {
        return buildString(bytebuf,PacketUtil.readVarInt(bytebuf));
    }
}
