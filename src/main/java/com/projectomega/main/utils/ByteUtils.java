package com.projectomega.main.utils;

import com.projectomega.main.game.Block;
import com.projectomega.main.game.Chunk;
import com.projectomega.main.packets.PacketUtil;
import com.projectomega.main.packets.datatype.VarLong;
import com.projectomega.main.versions.ProtocolManager;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

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
        ByteBuf buf = Unpooled.buffer();
        buf.writeInt(data);
        for(int i = 0; i < buf.writerIndex();i++){
            bytes[offset+i] = buf.readByte();
        }
        buf.release();
        return buf.writerIndex();
    }

    public static int addFloatToByteArray(byte[] bytes, int offset, float data) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeFloat(data);
        for(int i = 0; i < buf.writerIndex();i++){
            bytes[offset+i] = buf.readByte();
        }
        buf.release();
        return buf.writerIndex();
    }

    public static long getChunkSectionPositionAsALong(Chunk chunk, int y){
        return ((chunk.getX() & 0x3FFFFF) << 42) | (y & 0xFFFFF) | ((chunk.getZ() & 0x3FFFFF) << 20);
    }
    public static VarLong encodeBlockToBlocksArray(int protocolversion, Block block){
        int x = block.getLocation().getBlockX();
        int y = block.getLocation().getBlockY();
        int z = block.getLocation().getBlockZ();
        if(x < 0) {
            x = -x;
            x= 16-x;
        }
        if(y < 0) {
            y = -y;
            y= 16-y;
        }
        if(z < 0) {
            z = -z;
            z= 16-z;
        }

        long blockLocalX = x % 16;
        long blockLocalY = y % 16;
        long blockLocalZ = z % 16;

        return new VarLong(((long)(x+(16*z))/*ProtocolManager.getBlockIDByType(protocolversion, block.getType())*/) << 12 | (blockLocalX << 8 | blockLocalZ << 4 | blockLocalY));
    }

    public static int addDoubleToByteArray(byte[] bytes, int offset, double data) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeDouble(data);
        for(int i = 0; i < buf.writerIndex();i++){
            bytes[offset+i] = buf.readByte();
        }
        buf.release();
        return buf.writerIndex();
    }

    public static String buildString(ByteBuf bytebuf) {
        return buildString(bytebuf,PacketUtil.readVarInt(bytebuf));
    }
}
