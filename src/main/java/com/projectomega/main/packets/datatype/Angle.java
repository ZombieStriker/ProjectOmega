package com.projectomega.main.packets.datatype;

public class Angle {

    private final byte value;

    public Angle(float angle) {
        value = (byte) (angle * 256 / 360);
    }

    public byte getValue() {
        return value;
    }
}
