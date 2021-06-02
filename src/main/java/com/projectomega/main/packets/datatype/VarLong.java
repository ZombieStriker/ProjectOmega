package com.projectomega.main.packets.datatype;

public class VarLong {

    private final long value;

    public VarLong(long value) {
        this.value = value;
    }

    public long getLong() {
        return value;
    }
}
