package com.projectomega.main.utils;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class ByteUtils {

    public static int addIntegerToByteArray(byte[] bytes,int offset, int number){
        ByteBuffer buffer = ByteBuffer.allocate(3);
        buffer.putInt(number);
        for(int i = 0; i < 3 ; i++) {
            bytes[offset+i] = buffer.get(i);
        }
        return 3;
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
}
