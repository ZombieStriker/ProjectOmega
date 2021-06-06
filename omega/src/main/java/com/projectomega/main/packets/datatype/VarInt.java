package com.projectomega.main.packets.datatype;

public class VarInt {

    private final int value;

    public VarInt(int value) {
        this.value = value;
    }

    public int getInteger() {
        return value;
    }
}
