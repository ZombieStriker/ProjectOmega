package com.projectomega.main.packets.datatype;

public class UnsignedByte {

    private final byte unsignedByte;

    public UnsignedByte(int unsignedByte){
        this.unsignedByte = (byte) (unsignedByte+(unsignedByte > 127?(Byte.MIN_VALUE):0));
    }

    public byte getUnsignedByte(){
        return unsignedByte;
    }
}
