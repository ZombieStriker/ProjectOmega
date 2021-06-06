package com.projectomega.main.packets.datatype;

public class UnsignedByte {

    private final byte unsignedByte;

    public UnsignedByte(byte unsignedByte){
        this.unsignedByte = (byte) (unsignedByte+(Byte.MIN_VALUE));
    }

    public byte getUnsignedByte(){
        return unsignedByte;
    }
}
