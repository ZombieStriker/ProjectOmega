package com.projectomega.main.packets.datatype;

import com.projectomega.main.game.chat.TextMessage;
import com.projectomega.main.packets.PacketUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.nullicorn.nedit.NBTWriter;
import me.nullicorn.nedit.type.NBTCompound;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MetaData {

    private final Map<Integer, Object> metaData = new HashMap<>();

    public MetaData() {
        /*metaData.put(0,(byte)0x00);
        metaData.put(1,new VarInt(30));
        metaData.put(2,new JsonChatBuilder().add(new JsonChatElement("Test")));
        metaData.put(3,false);
        metaData.put(4,false);
        metaData.put(5,false);*/
    }

    public MetaData add(int index, Object obj) {
        metaData.put(index, obj);
        return this;
    }

    public Object get(int index) {
        return metaData.get(index);
    }

    public void set(int index, Object object) {
        metaData.put(index, object);
    }

    public byte[] build() {
        ByteBuf buf = Unpooled.buffer();
        int length = 0;

        for (Map.Entry<Integer, Object> object : metaData.entrySet()) {
            buf.writeByte((byte) (int) object.getKey());
            length += 1;
            if (object.getValue() instanceof Byte) {
                int varintlength = PacketUtil.writeVarInt(buf, 0, 0);
                length += varintlength;
                buf.writeByte((byte) object.getValue());
                length += 1;
            } else if (object.getValue() instanceof TextMessage) {
                int varintlength = PacketUtil.writeVarInt(buf, 0, 5);
                length += varintlength;
                buf.writeByte(1);
                length += 1;

                String json = ((TextMessage) object.getValue()).getAsJson();
                int stringlength = PacketUtil.writeVarInt(buf, 0, json.length());
                length += stringlength;
                int jsonlength = PacketUtil.addStringToByteArray(buf, 0, json);
                length += jsonlength;
            } else if (object.getValue() instanceof VarInt) {
                int varintlength = PacketUtil.writeVarInt(buf, 0, 1);
                length += varintlength;
                int varintlength2 = PacketUtil.writeVarInt(buf, 0, ((VarInt) object.getValue()).getInteger());
                length += varintlength2;
            }
            if (object.getValue() instanceof Boolean) {
                int varintlength = PacketUtil.writeVarInt(buf, 0, 7);
                length += varintlength;
                if ((boolean) object.getValue()) {
                    buf.writeByte(0x01);
                } else {
                    buf.writeByte(0x00);
                }
                length += 1;
            }
            if (object.getValue() instanceof Slot) {
                int varintlength = PacketUtil.writeVarInt(buf, 0, 6);
                length += varintlength;

                Slot slot = (Slot) object.getValue();


               /* buf.writeByte(0x01);
                length+=1;
                buf.writeByte(0x01);
                buf.writeByte(0x16);
                buf.writeByte(0x05);
                buf.writeByte(0x00);
                buf.writeByte(0x00);
                buf.writeByte(0x00);
                length+=6;*/

                buf.writeByte(0x01);
                length += 1;

                int varintlength2 = PacketUtil.writeVarInt(buf, 0, slot.id);
                length += varintlength2;

                buf.writeByte(slot.amount);
                length += 1;

                buf.writeByte(0x00);
                length += 1;

                /*
                byte[] value = new byte[4];
                int varintlength2 = ByteUtils.addShortToByteArray(value, 0,  slot.id);
                for (int i = 0; i < varintlength2; i++)
                    buf.writeByte(value[i]);
                length += varintlength2;


                buf.writeByte(slot.amount);
                length += 1;


                byte[] value2 = new byte[4];
                int varintlength3 = ByteUtils.addShortToByteArray(value2, 0,  slot.damage);
                for (int i = 0; i < varintlength3; i++)
                    buf.writeByte(value2[i]);
                length += varintlength3;


                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    NBTWriter.write(slot.tag, out, false);
                    byte[] byteArray = out.toByteArray();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Byte Array: ");
                    /*for (int i = 0; i < byteArray.length; i++) {
                        sb.append(byteArray[i] + " ");
                        buf.writeByte(byteArray[i]);
                        length += 1;
                    }* /
                    buf.writeByte(0);
                    length++;
                    sb.append("0 ");
                    System.out.println(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

            }
            if (object.getValue() instanceof NBTCompound) {
                int varintlength = PacketUtil.writeVarInt(buf, 0, 14);
                length += varintlength;
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    NBTWriter.write((NBTCompound) object.getValue(), out, false);
                    byte[] byteArray = out.toByteArray();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < byteArray.length; i++) {
                        sb.append(byteArray[i] + " ");
                        buf.writeByte(byteArray[i]);
                        length += 1;
                    }
                    buf.writeByte(0);
                    length++;
                    sb.append("0 ");
                    System.out.println(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] bytes = new byte[length + 1];
        StringBuilder debug = new StringBuilder();
        for (int i = 0; i < length; i++) {
            bytes[i] = buf.readByte();
            debug.append(bytes[i] + " ");
        }
        System.out.println(debug);
        bytes[bytes.length - 1] = (byte) 0xff;// new UnsignedByte((byte)0xff).getUnsignedBy
        return bytes;
    }
}
